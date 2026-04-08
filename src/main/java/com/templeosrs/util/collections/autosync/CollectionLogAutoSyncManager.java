package com.templeosrs.util.collections.autosync;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.gson.Gson;
import com.templeosrs.util.api.APIError;
import com.templeosrs.util.api.QuadraticBackoffStrategy;
import com.templeosrs.util.collections.CollectionLogManager;
import com.templeosrs.util.collections.CollectionLogRequestManager;
import com.templeosrs.util.collections.data.CollectionLogSyncResponse;
import com.templeosrs.util.collections.data.ObtainedCollectionItem;
import com.templeosrs.util.collections.data.PlayerProfile;
import com.templeosrs.util.collections.database.CollectionDatabase;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class CollectionLogAutoSyncManager
{
	@Getter
	protected final HashSet<String> obtainedItemNames = new HashSet<>();

	/**
	 * Keeps track of what item IDs are pending a server sync
	 */
	@Getter
	protected final HashSet<ObtainedCollectionItem> pendingSyncItems = new HashSet<>();

	@Getter
	QuadraticBackoffStrategy backoffStrategy = new QuadraticBackoffStrategy();

	@Inject
	private CollectionLogAutoSyncChatMessageSubscriber collectionLogAutoSyncChatMessageSubscriber;

	@Inject
	private CollectionLogAutoSyncItemContainerChangedSubscriber collectionLogAutoSyncItemContainerChangedSubscriber;

	@Inject
	private CollectionLogAutoSyncServerNpcLootSubscriber collectionLogAutoSyncServerNpcLootSubscriber;

	@Inject
	private CollectionLogAutoSyncGameTickSubscriber collectionLogAutoSyncGameTickSubscriber;

	@Inject
	private CollectionLogAutoSyncConfigChecker collectionLogAutoSyncConfigChecker;

	@Inject
	private LoggedInState loggedInState;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;

	@Inject
	private CollectionLogRequestManager requestManager;

	@Inject
	private CollectionLogManager collectionLogManager;

	@Inject
	private Gson gson;

	@Getter
	@Nullable
	private Integer gameTickToSync;

	@Getter
	@Setter
	private boolean triggerSyncAllowed;

	@Getter
	@Setter
	private boolean logOpenAutoSync;

	@Getter
	@Setter
	private boolean computingDiff;

	public void startUp()
	{
		eventBus.register(this);
		eventBus.register(collectionLogAutoSyncChatMessageSubscriber);
		eventBus.register(collectionLogAutoSyncItemContainerChangedSubscriber);
		eventBus.register(collectionLogAutoSyncServerNpcLootSubscriber);
		eventBus.register(collectionLogAutoSyncGameTickSubscriber);
		eventBus.register(collectionLogAutoSyncConfigChecker);
		eventBus.register(loggedInState);

		collectionLogAutoSyncConfigChecker.startUp();
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		eventBus.unregister(collectionLogAutoSyncChatMessageSubscriber);
		eventBus.unregister(collectionLogAutoSyncItemContainerChangedSubscriber);
		eventBus.unregister(collectionLogAutoSyncServerNpcLootSubscriber);
		eventBus.unregister(collectionLogAutoSyncGameTickSubscriber);
		eventBus.unregister(collectionLogAutoSyncConfigChecker);
		eventBus.unregister(loggedInState);

		collectionLogAutoSyncConfigChecker.shutDown();

		obtainedItemNames.clear();
		clearSyncCountdown();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == InterfaceID.COLLECTION)
		{
			setLogOpenAutoSync(true);
			setTriggerSyncAllowed(true);

			// Clear the previously obtained item list to avoid duplicating items when counts change
			collectionLogManager.getObtainedCollectionLogItems().clear();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (scriptPostFired.getScriptId() == 7797 && isTriggerSyncAllowed())
		{
			clientThread.invokeLater(() -> {
				client.menuAction(-1, 40697932, MenuAction.CC_OP, 1, -1, "Search", null);
				client.menuAction(-1, 40697932, MenuAction.CC_OP, 1, -1, "Back", null);

				setTriggerSyncAllowed(false);

				gameTickToSync = client.getTickCount() + 3;
			});
		}
	}

	/**
	 * Starts the sync countdown.
	 * This utilises a 17-tick delay (which corresponds to a roughly 10-second wait) as a way to batch requests.
	 * This is useful for when multiple items are obtained in quick succession, such as Chompy Hats.
	 */
	public void startSyncCountdown()
	{
		final int syncDelayInTicks = 17;

		gameTickToSync = client.getTickCount() + syncDelayInTicks;
	}

	/**
	 * Resets the sync countdown.
	 * Used after the request has successfully completed.
	 */
	public void clearSyncCountdown()
	{
		backoffStrategy.reset();
		setLogOpenAutoSync(false);
		gameTickToSync = null;
	}

	public void computeCollectionLogDiff()
	{
		try
		{
			String username = client.getLocalPlayer().getName();
			RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);

			// Prevent auto sync (on opening log) searching for new items in log when logged into a non-standard world or no data available
			if (username == null || !CollectionDatabase.hasPlayerData(username) || profileType.toString() != "STANDARD")
			{
				log.debug("No saved log items were found, falling back to a full sync for {}", username);

				clearSyncCountdown();

				return;
			}

			log.debug("Computing collection log diff for {}", username);

			Set<ObtainedCollectionItem> obtainedCollectionLogItems = collectionLogManager.getObtainedCollectionLogItems();

			final Multiset<Integer> collectionLogItemIdCountMap = HashMultiset.create();

			for (ObtainedCollectionItem item : obtainedCollectionLogItems)
			{
				final int itemId = item.getId();
				final int itemCount = item.getCount();

				collectionLogItemIdCountMap.add(itemId, itemCount);
			}

			final Multiset<Integer> itemDiff = CollectionDatabase.getCollectionLogDiff(username, collectionLogItemIdCountMap);

			if (itemDiff == null || itemDiff.isEmpty())
			{
				log.debug("No log items have been changed since the last sync for {}", username);

				// No items to sync, stop processing
				clearSyncCountdown();

				return;
			}

			log.debug("Found {} changed log item(s) since the last sync: {}", itemDiff.elementSet().size(), itemDiff);

			// Add the log items found in the diff to the pending sync items set
			obtainedCollectionLogItems
				.stream()
				// Name check isn't technically needed here, but it helps suppress warnings
				.filter(item -> itemDiff.contains(item.getId()) && item.getName() != null)
				.map(item -> new ObtainedCollectionItem(item.getId(), item.getName(), item.getCount()))
				.forEach(pendingSyncItems::add);
		}
		finally
		{
			setComputingDiff(false);
		}
	}

	/**
	 * Uploads the obtained collection log items to the server.
	 * This is called when the sync countdown has completed and there are items pending a sync.
	 */
	@Synchronized
	public void uploadObtainedCollectionLogItems()
	{
		if (backoffStrategy.shouldSkipRequest())
		{
			return;
		}

		String username = client.getLocalPlayer().getName();

		if (username == null)
		{
			return;
		}

		RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);
		PlayerProfile profileKey = new PlayerProfile(username, profileType);

		PlayerDataSync submission = new PlayerDataSync(
			profileKey.getUsername(),
			profileKey.getProfileType().name(),
			client.getAccountHash(),
			pendingSyncItems
		);

		try
		{
			String response = requestManager.uploadObtainedCollectionLogItems(submission);

			CollectionLogSyncResponse collectionLogSyncResponse = gson.fromJson(response, CollectionLogSyncResponse.class);
			String lastChangedTimestamp = getLastChangedTimestamp(collectionLogSyncResponse, response);

			log.debug("response: {}, lastChanged: {}", response, lastChangedTimestamp);

			// Saves the new/updated items to the API cache to prevent refetching the entire log
			CollectionDatabase.upsertItemsBatch(username, pendingSyncItems, Timestamp.valueOf(lastChangedTimestamp));

			obtainedItemNames.clear();
			pendingSyncItems.clear();

			clearSyncCountdown();

			log.debug("Successfully synchronised new log items for {}", submission.getUsername());
		}
		catch (IOException | NullPointerException e)
		{
			log.error("❌ Failed to upload obtained collection log items: {}", e.getMessage());
		}
		finally
		{
			backoffStrategy.finishCycle();
		}
	}

	private String getLastChangedTimestamp(CollectionLogSyncResponse collectionLogSyncResponse, String response) throws IOException
	{
		APIError error = collectionLogSyncResponse.getError();
		CollectionLogSyncResponse.Data data = collectionLogSyncResponse.getData();

		if (error == null && data == null)
		{
			clearSyncCountdown();

			throw new NullPointerException("Unexpected response format from the collection log sync endpoint: " + response);
		}

		if (error != null)
		{
			clearSyncCountdown();

			throw new IOException(String.valueOf(error));
		}

		return data.getLastChanged();
	}
}
