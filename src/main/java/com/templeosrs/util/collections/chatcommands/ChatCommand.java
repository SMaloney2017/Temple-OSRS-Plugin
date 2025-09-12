package com.templeosrs.util.collections.chatcommands;

import com.templeosrs.util.collections.CollectionLogCategoryGroup;
import com.templeosrs.util.collections.CollectionLogManager;
import com.templeosrs.util.collections.utils.CollectionLogCategoryUtils;
import com.templeosrs.util.collections.utils.PlayerNameUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Set;

@Slf4j
public abstract class ChatCommand {
    public ChatCommand(String trigger, String description, boolean onlyShowForLocalPlayer)
    {
        this.trigger = trigger;
        this.description = description;
        this.onlyShowForLocalPlayer = onlyShowForLocalPlayer;
    }

    @Inject
    protected Client client;

    @Inject
    protected ClientThread clientThread;

    @Inject
    protected ChatMessageManager chatMessageManager;

    @Inject
    protected ItemManager itemManager;

	@Inject
	protected ItemSpriteManager itemSpriteManager;

    /**
     * The chat message that triggers the command, e.g. "!col help"
     */
    public String trigger;

    /**
     * The description given to the command when listed by the "!col help" command
     */
    public String description;

    /**
     * If true, hide the message from all other players except the local player
     */
    private final boolean onlyShowForLocalPlayer;

	/**
     * Checks whether the message sender is the currently logged in player
     *
     * @param event the ChatMessage event
     * @return true if the message sender if the currently logged in player
     */
    public boolean isOtherPlayer(ChatMessage event)
    {
        String localName = PlayerNameUtils.normalizePlayerName(client.getLocalPlayer().getName());
        String senderName = PlayerNameUtils.normalizePlayerName(event.getName());

        return !senderName.equalsIgnoreCase(localName);
    }

    public String buildAvailableCategoriesMessage(String category)
    {
        return new ChatMessageBuilder()
            .append(ChatColorType.NORMAL)
            .append("Available ")
            .append(ChatColorType.HIGHLIGHT)
            .append(category)
            .append(ChatColorType.NORMAL)
            .append(" categories:")
            .build();
    }

    public void overwriteMessage(String newMessage, MessageNode messageNode)
    {
        messageNode.setRuneLiteFormatMessage(newMessage);
        client.refreshChat();
    }

    /**
     * Outputs a list of all available collection log categories.
     * As this is derived from the in-game cache,
     * it will always be up-to-date with the latest changes (unless a new tab is added).
     *
     * @param categoryGroup The category group (i.e. tab) for which to list items
     */
    public void listAvailableCollectionLogCategories(CollectionLogCategoryGroup categoryGroup)
    {
        clientThread.invoke(() -> {
            chatMessageManager.queue(
                QueuedMessage.builder()
                    .type(ChatMessageType.CONSOLE)
                    .runeLiteFormattedMessage(buildAvailableCategoriesMessage(categoryGroup.toString()))
                    .build()
            );

            Set<String> categorySlugs = CollectionLogManager
                    .getCollectionLogCategoryTabSlugs()
                    .get(categoryGroup.getStructId());

			List<Integer> iconItemIds = categorySlugs.stream().map(slug -> {
				int structId = CollectionLogManager.getCollectionLogCategoryStructIdMap().get(slug);
				Set<Integer> categoryItems = CollectionLogManager.getCollectionLogCategoryItemMap().get(structId);

				return new ArrayList<>(categoryItems).get(0);
			}).collect(Collectors.toList());

			itemSpriteManager.loadItemSprites(iconItemIds);

			int i = 0;

            for (String categorySlug : categorySlugs) {
                int structId = CollectionLogManager.getCollectionLogCategoryStructIdMap().get(categorySlug);
                String categoryTitle = client.getStructComposition(structId).getStringValue(689);
				int iconIndex = itemSpriteManager.getItemSpriteIndexes().get(iconItemIds.get(i++));

				ChatMessageBuilder chatMessageBuilder = new ChatMessageBuilder()
                        .append(ChatColorType.NORMAL)
						.img(iconIndex)
                        .append(categoryTitle)
                        .append(": ");

                Set<String> categoryAliases = CollectionLogCategoryUtils.INVERTED_ALIASES.get(categorySlug);

                chatMessageBuilder
                        .append(ChatColorType.HIGHLIGHT)
                        .append(
                            categoryAliases == null
                                ? categorySlug
                                : String.join(", ", categoryAliases)
                        );

                chatMessageManager.queue(
                    QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(chatMessageBuilder.build())
                        .build()
                );
            }

            client.refreshChat();
        });
    }

    /**
     * Executes the chat command handler.
     * If it has been configured to only show for the current player, it will not be triggered for anyone else.
     * @param event The chat message event
     */
    public void execute(ChatMessage event)
    {
        if (onlyShowForLocalPlayer && isOtherPlayer(event)) {
            return;
        }

        command(event);
    }

    public void command(ChatMessage event) {}

    public void shutDown() {
		itemSpriteManager.itemSpriteIndexes.clear();
		itemSpriteManager.loadedItemIds.clear();
	}
}
