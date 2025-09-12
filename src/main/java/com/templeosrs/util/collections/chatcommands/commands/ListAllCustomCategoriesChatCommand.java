package com.templeosrs.util.collections.chatcommands.commands;

import com.templeosrs.util.collections.chatcommands.ChatCommand;
import com.templeosrs.util.collections.data.CollectionLogCategory;
import com.templeosrs.util.collections.utils.CollectionLogCategoryUtils;
import java.util.List;
import java.util.stream.Collectors;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.QueuedMessage;

import java.util.Map;

public class ListAllCustomCategoriesChatCommand extends ChatCommand {
    public ListAllCustomCategoriesChatCommand()
    {
        super("!col list custom", "Lists all available custom categories", true);
    }

    @Override
    public void command(ChatMessage event)
    {
        clientThread.invoke(() -> {
            chatMessageManager.queue(
                QueuedMessage.builder()
                    .type(ChatMessageType.CONSOLE)
                    .runeLiteFormattedMessage(buildAvailableCategoriesMessage("custom"))
                    .build()
            );

			List<Integer> iconItemIds = CollectionLogCategoryUtils.CUSTOM_CATEGORIES
				.values()
				.stream()
				.map(item -> item.getItems().iterator().next())
				.collect(Collectors.toList());

			itemSpriteManager.loadItemSprites(iconItemIds);

			int i = 0;

            for (Map.Entry<String, CollectionLogCategory> customCategory : CollectionLogCategoryUtils.CUSTOM_CATEGORIES.entrySet()) {
				int iconIndex = itemSpriteManager.getItemSpriteIndexes().get(iconItemIds.get(i++));

                chatMessageManager.queue(
                    QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(
                            new ChatMessageBuilder()
                                .append(ChatColorType.NORMAL)
								.img(iconIndex)
                                .append(customCategory.getValue().getTitle())
                                .append(": ")
                                .append(ChatColorType.HIGHLIGHT)
                                .append(customCategory.getKey())
                                .build()
                        )
                        .build()
                );
            }

            client.refreshChat();
        });
    }
}
