package com.templeosrs.util.collections.chatcommands;

import com.templeosrs.util.collections.chatcommands.commands.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.*;

@Slf4j
public class CollectionLogChatCommandChatMessageSubscriber
{
	@Inject
	private Client client;

	@Inject
	private EventBus eventBus;

	@Inject
	private DisplayPlayerCollectionLogChatCommand displayPlayerCollectionLogChatCommand;

	@Inject
	private ListAllHelpCommandsChatCommand helpCommandsChatCommand;

	@Inject
	private ListAllBossCategoriesChatCommand bossCategoriesChatCommand;

	@Inject
	private ListAllCluesCategoriesChatCommand cluesCategoriesChatCommand;

	@Inject
	private ListAllRaidsCategoriesChatCommand raidsCategoriesChatCommand;

	@Inject
	private ListAllCustomCategoriesChatCommand customCategoriesChatCommand;

	@Inject
	private ListAllMinigamesCategoriesChatCommand minigamesCategoriesChatCommand;

	@Inject
	private ListAllOtherCategoriesChatCommand otherCategoriesChatCommand;

	private final Set<ChatMessageType> allowedMessageTypes = Set.of(
		ChatMessageType.PUBLICCHAT,
		ChatMessageType.FRIENDSCHAT,
		ChatMessageType.PRIVATECHAT,
		ChatMessageType.PRIVATECHATOUT,
		ChatMessageType.CLAN_CHAT,
		ChatMessageType.CLAN_GIM_MESSAGE,
		ChatMessageType.CLAN_GIM_CHAT,
		ChatMessageType.CLAN_GUEST_CHAT
	);

	public static Map<String, ChatCommand> chatCommands = new LinkedHashMap<>();

	public void startUp()
	{
		eventBus.register(this);

		chatCommands.put(displayPlayerCollectionLogChatCommand.trigger, displayPlayerCollectionLogChatCommand);
		chatCommands.put(helpCommandsChatCommand.trigger, helpCommandsChatCommand);
		chatCommands.put(bossCategoriesChatCommand.trigger, bossCategoriesChatCommand);
		chatCommands.put(raidsCategoriesChatCommand.trigger, raidsCategoriesChatCommand);
		chatCommands.put(cluesCategoriesChatCommand.trigger, cluesCategoriesChatCommand);
		chatCommands.put(minigamesCategoriesChatCommand.trigger, minigamesCategoriesChatCommand);
		chatCommands.put(otherCategoriesChatCommand.trigger, otherCategoriesChatCommand);
		chatCommands.put(customCategoriesChatCommand.trigger, customCategoriesChatCommand);
	}

	public void shutDown()
	{
		eventBus.unregister(this);

		chatCommands.values().forEach(ChatCommand::shutDown);
		chatCommands.clear();
	}

	@Subscribe(priority = -2) // Run after ChatMessageManager
	public void onChatMessage(ChatMessage event)
	{
		final ChatMessageType type = event.getType();
		final String rawMessage = event.getMessage().trim();

		final String COLLECTION_LOG_CHAT_TRIGGER = "!col ";

		// Only react to public, private, or clan chat messages that begin with "!col "
		if (!allowedMessageTypes.contains(type) || !rawMessage.toLowerCase().startsWith(COLLECTION_LOG_CHAT_TRIGGER))
		{
			return;
		}

		ChatCommand chatCommand = chatCommands.get(rawMessage.toLowerCase());

		if (chatCommand != null)
		{
			chatCommand.execute(event);

			return;
		}

		displayPlayerCollectionLogChatCommand.command(event);
	}

	/**
	 * Prevents the help commands from being rendered to the chat for anyone with the plugin installed
	 */
	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event)
	{
		if (!event.getEventName().equals("chatFilterCheck"))
		{
			return;
		}

		int[] intStack = client.getIntStack();
		int intStackSize = client.getIntStackSize();
		Object[] objectStack = client.getObjectStack();
		int objectStackSize = client.getObjectStackSize();

		final int messageType = intStack[intStackSize - 2];
		String message = (String) objectStack[objectStackSize - 1];
		ChatMessageType chatMessageType = ChatMessageType.of(messageType);

		if (!allowedMessageTypes.contains(chatMessageType))
		{
			return;
		}

		Set<String> hiddenChatCommands = Set.of("!col list", "!col help");

		for (String hiddenChatCommand : hiddenChatCommands)
		{
			if (message.toLowerCase().startsWith(hiddenChatCommand))
			{
				intStack[intStackSize - 3] = 0;

				break;
			}
		}
	}
}
