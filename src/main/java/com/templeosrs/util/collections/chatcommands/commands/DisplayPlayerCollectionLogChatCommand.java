package com.templeosrs.util.collections.chatcommands.commands;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.collections.CollectionLogCategorySlug;
import com.templeosrs.util.collections.CollectionLogManager;
import com.templeosrs.util.collections.CollectionLogRequestManager;
import com.templeosrs.util.collections.chatcommands.ChatCommand;
import com.templeosrs.util.collections.data.CollectionLogCategory;
import com.templeosrs.util.collections.data.ObtainedCollectionItem;
import com.templeosrs.util.collections.data.PlayerInfoResponse;
import com.templeosrs.util.collections.database.CollectionDatabase;
import com.templeosrs.util.collections.parser.CollectionParser;
import com.templeosrs.util.collections.services.CollectionLogService;
import com.templeosrs.util.collections.utils.CollectionLogCategoryUtils;
import com.templeosrs.util.collections.utils.PlayerNameUtils;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.StructComposition;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class DisplayPlayerCollectionLogChatCommand extends ChatCommand  {
    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    private CollectionLogRequestManager collectionLogRequestManager;

    @Inject
    private TempleOSRSPlugin templeOSRSPlugin;

    @Inject
    private CollectionParser collectionParser;

    @Inject
    private CollectionLogService collectionLogService;

    public DisplayPlayerCollectionLogChatCommand()
	{
		super("!col ", "Displays the player's collection log for a given boss. May also be used to display other players' logs, e.g. !col kree CousinOfKos", false);
	}

    @Override
    public void command(ChatMessage event)
	{
        final String rawMessage = event.getMessage().trim();

        String[] parts = rawMessage.substring(5).trim().split(" ", 2);

        if (parts.length == 0) {
            return;
        }

        // Normalize boss name
        String bossInput = parts[0].trim().replace(' ', '_').toLowerCase();
        CollectionLogCategory category = getCategoryFromMessageInput(bossInput);

        // Determine target player (specified or sender)
        String playerName = (parts.length == 2) ? parts[1].trim() : event.getName();
        String normalizedPlayerName = PlayerNameUtils.normalizePlayerName(playerName);  // Normalize the player name for the API call
        String localName = PlayerNameUtils.normalizePlayerName(client.getLocalPlayer().getName());
        boolean isLocalPlayer = normalizedPlayerName.equalsIgnoreCase(localName);

        if (category == null) {
            if (isLocalPlayer) {
                log.warn("❌ No alias or category found for {}", bossInput);

                final String errorMessage = new ChatMessageBuilder()
                    .append(ChatColorType.NORMAL)
                    .append("Use ")
                    .append(ChatColorType.HIGHLIGHT)
                    .append("!col help")
                    .append(ChatColorType.NORMAL)
                    .append(" to help find the correct category.")
                    .build();

                chatMessageManager.queue(
                    QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(errorMessage)
                        .build()
                );
            }

            overwriteMessage(
                new ChatMessageBuilder()
                    .append(ChatColorType.HIGHLIGHT)
                    .append(bossInput)
                    .append(ChatColorType.NORMAL)
                    .append(" is not a valid collection log category or alias.")
                    .build(),
                event.getMessageNode()
            );

            return;
        }

        scheduledExecutorService.execute(() ->
        {
            PlayerInfoResponse.Data playerInfo = getPlayerInfo(normalizedPlayerName, event);

            if (playerInfo == null) {
                // Error messages are handled when getting the player info
                return;
            }

            String prettyPlayerName = playerInfo.getPlayerNameWithCapitalization();

            if (playerInfo.getCollectionLog().getLastChanged() == null) {
                overwriteMessage(
                    new ChatMessageBuilder()
                        .append(ChatColorType.NORMAL)
                        .append("No TempleOSRS collection log found for ")
                        .append(ChatColorType.HIGHLIGHT)
                        .append(prettyPlayerName)
                        .append(".")
                        .build(),
                    event.getMessageNode()
                );

                return;
            }

            String lastChanged = playerInfo.getCollectionLog().getLastChanged();

            final boolean isDataStale = !collectionLogService.isDataFresh(normalizedPlayerName, lastChanged);
            final boolean hasLocalData = CollectionDatabase.hasPlayerData(normalizedPlayerName);
            final boolean shouldUpdate = !hasLocalData || isDataStale;

            if (shouldUpdate)
            {
                log.debug("📭 No local data for '{}', fetching from API...", normalizedPlayerName);
                String json = collectionLogRequestManager.getPlayerCollectionLog(normalizedPlayerName);

                if (json == null) {
                    log.warn("❌ No data fetched for user: {}", normalizedPlayerName);

                    overwriteMessage(
                        new ChatMessageBuilder()
                            .append(ChatColorType.NORMAL)
                            .append("Failed to fetch log for ")
                            .append(ChatColorType.HIGHLIGHT)
                            .append(prettyPlayerName)
                            .append(ChatColorType.NORMAL)
                            .append(".")
                            .build(),
                        event.getMessageNode()
                    );

                    return;
                }

                if (!isLocalPlayer) {
                    CollectionDatabase.pruneOldPlayers(localName, templeOSRSPlugin.getConfig().maxCachedPlayers());
                }

                collectionParser.parseAndStore(PlayerNameUtils.normalizePlayerName(playerName), json);
            }
            else
            {
                log.debug("✔️ Found cached data for '{}'", normalizedPlayerName);
            }

            // Fetch the requested category
            Set<ObtainedCollectionItem> items = CollectionDatabase.getItemsByCategory(
                    normalizedPlayerName,
					new LinkedHashSet<>(category.getItems())
            );

            itemSpriteManager.loadItemSprites(
				items
					.stream()
					.map(ObtainedCollectionItem::getId)
					.collect(Collectors.toList())
			);

            ChatMessageBuilder chatMessageBuilder = new ChatMessageBuilder();
            String categoryName = category.getTitle();


			// If sender's name is same as the player being queried, omit the player's name
			if (!event.getName().equalsIgnoreCase(playerName))
			{
				chatMessageBuilder
					.append(ChatColorType.HIGHLIGHT)
					.append(prettyPlayerName + "'s ")
					.append(categoryName)
					.append(ChatColorType.NORMAL);
			}
			else
			{
				chatMessageBuilder.append(categoryName);
			}

			chatMessageBuilder
				.append(getLogProgress(items.size(), category.getItems().size()))
				.append(": ");

            if (items.isEmpty()) {
                chatMessageBuilder.append("No obtained collection log items.");
            } else {
                int i = 0;

                for (ObtainedCollectionItem item : items)
                {
                    Integer iconIndex = itemSpriteManager.getItemSpriteIndexes().get(item.getId());

                    if (iconIndex != null) {
                        chatMessageBuilder.img(iconIndex);
                    }

                    chatMessageBuilder
                            .append("x")
                            .append(String.valueOf(item.getCount()));

                    if (i++ < items.size() - 1) {
                        chatMessageBuilder.append(", ");
                    }
                }
            }

            overwriteMessage(chatMessageBuilder.build(), event.getMessageNode());
        });
    }

    private PlayerInfoResponse.Data getPlayerInfo(String playerName, ChatMessage chatMessage)
    {
        try {
            return collectionLogRequestManager.getPlayerInfo(playerName);
        } catch (NullPointerException e) {
            overwriteMessage(
                new ChatMessageBuilder()
                    .append(ChatColorType.NORMAL)
                    .append("Unable to find ")
                    .append(ChatColorType.HIGHLIGHT)
                    .append(playerName)
                    .append(ChatColorType.NORMAL)
                    .append(" on TempleOSRS.")
                    .build(),
                chatMessage.getMessageNode()
            );
        } catch (IOException e) {
            overwriteMessage(
                new ChatMessageBuilder()
                    .append(ChatColorType.NORMAL)
                    .append("Failed to fetch from TempleOSRS.")
                    .build(),
                chatMessage.getMessageNode()
            );
        }

        return null;
    }

    private String getCategoryKeyFromMessageInput(String bossInput)
    {
        try {
            final CollectionLogCategorySlug categorySlug = Objects.requireNonNullElseGet(
                    CollectionLogCategoryUtils.CATEGORY_ALIASES.get(bossInput),
                    () -> CollectionLogCategorySlug.valueOf(bossInput)
            );

            return categorySlug.toString();
        } catch (IllegalArgumentException e) {
            return bossInput;
        }
    }

    private CollectionLogCategory getCategoryFromMessageInput(String bossInput)
    {
        String categoryKey = getCategoryKeyFromMessageInput(bossInput);
        CollectionLogCategory customCategory = CollectionLogCategoryUtils.CUSTOM_CATEGORIES.get(bossInput);

        if (customCategory != null) {
            return customCategory;
        }

        try {
            int structId = CollectionLogManager.getCollectionLogCategoryStructIdMap().get(categoryKey);

            StructComposition categoryStruct = client.getStructComposition(structId);
            String categoryTitle = categoryStruct.getStringValue(689);
            Set<Integer> categoryItems = CollectionLogManager.getCollectionLogCategoryItemMap().get(categoryStruct.getId());

			return new CollectionLogCategory(categoryTitle, categoryItems);
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	private String getLogProgress(int acquiredItemsCount, int totalItemsCount)
	{
		if (acquiredItemsCount == 0)
		{
			return "";
		}

		return " (" + acquiredItemsCount + "/" + totalItemsCount + ")";
	}
}
