/*
 * Copyright (c) 2025, andmcadams
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.templeosrs.util.collections;

import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.api.QuadraticBackoffStrategy;
import com.templeosrs.util.collections.autosync.CollectionLogAutoSyncManager;
import com.templeosrs.util.collections.chatcommands.CollectionLogChatCommandChatMessageSubscriber;
import com.templeosrs.util.collections.data.*;
import com.templeosrs.util.collections.database.CollectionDatabase;
import com.templeosrs.util.collections.services.CollectionLogService;
import com.templeosrs.util.collections.utils.CollectionLogCacheData;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class CollectionLogManager
{
	@Inject
	private ItemManager itemManager;

	@Inject
	private SyncButtonManager syncButtonManager;

	@Inject
	private ScheduledExecutorService scheduledExecutorService;

	@Getter
	@Inject
	private Client client;

	@Getter
	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;

	@Inject
	private CollectionLogAutoSyncManager collectionLogAutoSyncManager;

	@Inject
	private TempleOSRSPlugin templeOSRSPlugin;

	@Inject
	private CollectionLogRequestManager requestManager;

	@Inject
	private CollectionLogService collectionLogService;

	@Inject
	private CollectionLogChatCommandChatMessageSubscriber collectionLogChatCommandChatMessageSubscriber;

	@Inject
	private CollectionLogRequestManager collectionLogRequestManager;

	@Nullable
	private Integer gameTickToSync;

	/**
	 * List of items found in the collection log, computed by reading the in-game enums/structs.
	 */
	private final Set<Integer> collectionLogItemsFromCache = new HashSet<>();

	/**
	 * Unique list of all obtained collection log items
	 */
	@Getter
	private final Set<ObtainedCollectionItem> obtainedCollectionLogItems = new HashSet<>();

	/**
	 * Maps in-game categories to the list of items they contain. Pulled from the in-game cache, where the key is the
	 * category struct ID (e.g. <a href="https://chisel.weirdgloop.org/structs/index.html?type=structs&id=493">STRUCT #493</a>)
	 * and the value is the contents of the enum found in <a href="https://chisel.weirdgloop.org/structs/index.html?type=params&id=690">PARAM #690</a>.
	 */
	@Getter
	private static final Map<Integer, Set<Integer>> collectionLogCategoryItemMap = new HashMap<>();

	/**
	 * Maps slugified category names (e.g. guardians_of_the_rift) to their in-game struct ID
	 */
	@Getter
	private static final Map<String, Integer> collectionLogCategoryStructIdMap = new HashMap<>();

	/**
	 * Maps top level tabs (e.g. "Bosses") to their containing categories.
	 * Used to list the available categories when using the "!col help ___" commands
	 */
	@Getter
	private static final Map<Integer, Set<String>> collectionLogCategoryTabSlugs = new LinkedHashMap<>();

	@Getter
	private final QuadraticBackoffStrategy backoffStrategy = new QuadraticBackoffStrategy();

	public void startUp()
	{
		eventBus.register(this);

		CollectionDatabase.init();

		if (templeOSRSPlugin.getConfig().enableClogChatCommand())
		{
			collectionLogChatCommandChatMessageSubscriber.startUp();
		}

		if (templeOSRSPlugin.getConfig().autoSyncClog())
		{
			collectionLogAutoSyncManager.startUp();
		}

		clientThread.invoke(() -> {
			if (client.getIndexConfig() == null || client.getGameState().ordinal() < GameState.LOGIN_SCREEN.ordinal())
			{
				return false;
			}

			CollectionLogCacheData collectionLogCacheData = parseCacheForClog();

			collectionLogItemsFromCache.addAll(collectionLogCacheData.getItemIds());
			collectionLogCategoryItemMap.putAll(collectionLogCacheData.getCategoryItems());
			collectionLogCategoryStructIdMap.putAll(collectionLogCacheData.getCategoryStructIds());
			collectionLogCategoryTabSlugs.putAll(collectionLogCacheData.getCategorySlugs());

			return true;
		});
	}

	public void shutDown()
	{
		eventBus.unregister(this);

		if (templeOSRSPlugin.getConfig().enableClogChatCommand())
		{
			collectionLogChatCommandChatMessageSubscriber.shutDown();
		}

		if (templeOSRSPlugin.getConfig().autoSyncClog())
		{
			collectionLogAutoSyncManager.shutDown();
		}

		syncButtonManager.shutDown();

		obtainedCollectionLogItems.clear();
		collectionLogItemsFromCache.clear();
		collectionLogCategoryItemMap.clear();
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged configChanged)
	{
		if (!configChanged.getGroup().equals(TempleOSRSConfig.TEMPLE_OSRS_CONFIG_GROUP))
		{
			return;
		}

		if (templeOSRSPlugin.getConfig().autoSyncClog())
		{
			collectionLogAutoSyncManager.startUp();
		}
		else
		{
			collectionLogAutoSyncManager.shutDown();
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (backoffStrategy.isRequestLimitReached())
		{
			gameTickToSync = null;

			return;
		}

		if (!backoffStrategy.isSubmitting() && gameTickToSync != null && client.getTickCount() >= gameTickToSync)
		{
			backoffStrategy.setSubmitting(true);
			scheduledExecutorService.execute(this::submitTask);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		GameState state = gameStateChanged.getGameState();

		switch (state)
		{
			// When hopping, we need to clear any state related to the player
			case HOPPING:
			case LOGGING_IN:
			case CONNECTION_LOST:
				break;
			case LOGGED_IN:
			{
				// Attempt to synchronise the player's collection log on login
				clientThread.invokeLater(() -> {
					final String username = client.getLocalPlayer().getName();

					// Wait for username to be available
					if (username == null)
					{
						return false;
					}

					try
					{
						String lastChanged = collectionLogRequestManager
							.getPlayerInfo(username)
							.getCollectionLog()
							.getLastChanged();


						// Skip sync if the player's collection log doesn't exist, or has already been saved and is up-to-date
						if (
							lastChanged == null ||
								(collectionLogService.isDataFresh(username, lastChanged) && CollectionDatabase.hasPlayerData(username))
						)
						{
							return true;
						}

						collectionLogService.syncCollectionLog();

						return true;
					}
					catch (NullPointerException | IOException e)
					{
						// If an error occurs then bail early to avoid infinite retries
						return true;
					}
				});
			}
		}
	}

	/**
	 * Handles updating the collection log after the log has opened or the Temple sync button has been pressed.
	 */
	@Subscribe
	public void onScriptPreFired(ScriptPreFired preFired)
	{
		if (preFired.getScriptId() == 4100)
		{
			if (collectionLogItemsFromCache.isEmpty())
			{
				return;
			}

			// Submit the collection log data three ticks after the first script prefires
			// This gives the game time to build the obtained items set and resolves an issue
			// that caused players with large logs to miss items in their sync data
			gameTickToSync = client.getTickCount() + 3;

			Object[] args = preFired.getScriptEvent().getArguments();
			int itemId = (int) args[1];
			int itemCount = (int) args[2];
			String itemName = itemManager.getItemComposition(itemId).getName();

			obtainedCollectionLogItems.add(new ObtainedCollectionItem(itemId, itemName, itemCount));
		}
	}

	@Synchronized
	public void submitTask()
	{
		// TODO: do we want other GameStates?
		if (client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer().getName() == null)
		{
			log.error("⚠️ Aborting sync as the player is no longer logged in");

			return;
		}

		if (backoffStrategy.shouldSkipRequest())
		{
			return;
		}

		String username = client.getLocalPlayer().getName();

		final boolean hasPlayerData = CollectionDatabase.hasPlayerData(username);

		if (hasPlayerData && !syncButtonManager.isFullSyncRequested())
		{
			backoffStrategy.reset();
			gameTickToSync = null;

			return;
		}

		// If no API player data exists or if the sync button has been pressed, upload the entire log
		submitPlayerData();
	}

	private void submitPlayerData()
	{
		String username = client.getLocalPlayer().getName();

		// Do not send if slot data wasn't generated
		if (obtainedCollectionLogItems.isEmpty())
		{
			log.error("❌ No obtained items have been set for {}", username);

			gameTickToSync = null;
			backoffStrategy.reset();

			return;
		}

		RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);
		PlayerProfile profileKey = new PlayerProfile(username, profileType);

		// Only IDs and counts are useful in the request
		Set<ObtainedCollectionItem> preparedItems = obtainedCollectionLogItems
			.stream()
			.map(item -> new ObtainedCollectionItem(item.getId(), item.getCount()))
			.collect(Collectors.toSet());

		int totalCollectionsAvailable = collectionLogItemsFromCache.size();

		PlayerData playerData = new PlayerData(totalCollectionsAvailable, preparedItems);

		PlayerDataSubmission submission = new PlayerDataSubmission(
			profileKey.getUsername(),
			profileKey.getProfileType().name(),
			client.getAccountHash(),
			playerData
		);

		try
		{
			requestManager.uploadFullCollectionLog(submission);
			syncButtonManager.setFullSyncRequested(false);
			gameTickToSync = null;

			log.debug("Successfully submitted collection log for {}", submission.getUsername());
		}
		catch (IOException e)
		{
			log.error("❌ Failed to upload collection log for {}", submission.getUsername());
		}
		finally
		{
			backoffStrategy.finishCycle();
		}
	}

	/**
	 * Parse the enums and structs in the cache to figure out which item ids exist in the collection log.
	 */
	private CollectionLogCacheData parseCacheForClog()
	{
		Set<Integer> items = new HashSet<>();
		Map<Integer, Set<Integer>> categoryItems = new HashMap<>();
		Map<String, Integer> categoryStructIds = new HashMap<>();
		Map<Integer, Set<String>> categorySlugs = new LinkedHashMap<>();

		final Pattern specialCharacterPattern = Pattern.compile("['()]", Pattern.CASE_INSENSITIVE);

		// Some items with data saved on them have replacements to fix a duping issue (satchels, flamtaer bag)
		// Enum 3721 contains a mapping of the item ids to replace -> ids to replace them with
		EnumComposition replacements = client.getEnum(3721);

		// 2102 - Struct that contains the highest level tabs in the collection log (Bosses, Raids, etc)
		// https://chisel.weirdgloop.org/structs/index.html?type=enums&id=2102
		int[] topLevelTabStructIds = client.getEnum(2102).getIntVals();
		for (int topLevelTabStructIndex : topLevelTabStructIds)
		{
			// The collection log top level tab structs contain a param that points to the enum
			// that contains the pointers to sub tabs.
			// ex: https://chisel.weirdgloop.org/structs/index.html?type=structs&id=471
			StructComposition topLevelTabStruct = client.getStructComposition(topLevelTabStructIndex);

			Set<String> singleCategorySlugSet = new LinkedHashSet<>();

			// Param 683 contains the pointer to the enum that contains the subtabs ids
			// ex: https://chisel.weirdgloop.org/structs/index.html?type=enums&id=2103
			int[] subtabStructIndices = client.getEnum(topLevelTabStruct.getIntValue(683)).getIntVals();
			for (int subtabStructIndex : subtabStructIndices)
			{

				// The subtab structs are for subtabs in the collection log (Commander Zilyana, Chambers of Xeric, etc.)
				// and contain a pointer to the enum that contains all the item ids for that tab.
				// ex subtab struct: https://chisel.weirdgloop.org/structs/index.html?type=structs&id=476
				// ex subtab enum: https://chisel.weirdgloop.org/structs/index.html?type=enums&id=2109
				StructComposition subtabStruct = client.getStructComposition(subtabStructIndex);

				int[] clogItems = client.getEnum(subtabStruct.getIntValue(690)).getIntVals();

				// Gets a slugified version of the category title
				// (e.g. Master Treasure Trails (Rare) -> master_treasure_trails_rare)
				String normalizedCategoryName = specialCharacterPattern
					.matcher(
						subtabStruct.getStringValue(689)
							.toLowerCase()
							.replaceAll(" ", "_")
					)
					.replaceAll("");

				Set<Integer> itemSet = new LinkedHashSet<>();

				for (int clogItemId : clogItems)
				{
					final int replacementId = replacements.getIntValue(clogItemId);

					itemSet.add(
						replacementId == -1
							? clogItemId
							: replacementId
					);
				}

				items.addAll(itemSet);
				categoryItems.put(subtabStructIndex, itemSet);
				categoryStructIds.put(normalizedCategoryName, subtabStructIndex);
				singleCategorySlugSet.add(normalizedCategoryName);
			}

			categorySlugs.put(topLevelTabStructIndex, singleCategorySlugSet);
		}

		return new CollectionLogCacheData(items, categoryItems, categoryStructIds, categorySlugs);
	}
}
