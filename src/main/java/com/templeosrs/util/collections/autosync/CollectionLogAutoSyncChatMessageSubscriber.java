package com.templeosrs.util.collections.autosync;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.Client;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.RuneScapeProfileType;
import static net.runelite.client.util.Text.removeTags;

@Slf4j
public class CollectionLogAutoSyncChatMessageSubscriber
{
	private final Pattern NEW_COLLECTION_LOG_ITEM_PATTERN = Pattern.compile("New item added to your collection log: (.*)");

	@Inject
	private CollectionLogAutoSyncManager collectionLogAutoSyncManager;

	@Inject
	private Client client;

	/**
	 * This method watches for in-game chat messages that match the new collection log item pattern.
	 * If a matching message is found, it extracts the item name and adds it to the obtainedItemNames set,
	 * which is then used by the ItemContainerChanged and NpcLootReceived events to determine if the item should be synced.
	 */
	@Subscribe
	private void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		Matcher matcher = NEW_COLLECTION_LOG_ITEM_PATTERN.matcher(chatMessage.getMessage());

		if (matcher.matches())
		{
			// Only sync items on regular worlds
			RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);

			if (profileType.toString() != "STANDARD")
			{
				return;
			}

			String itemName = removeTags(matcher.group(1));

			collectionLogAutoSyncManager.obtainedItemNames.add(itemName);
		}
	}
}
