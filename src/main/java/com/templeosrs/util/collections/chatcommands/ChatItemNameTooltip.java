package com.templeosrs.util.collections.chatcommands;

import com.templeosrs.TempleOSRSConfig;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.Text;

@Slf4j
public class ChatItemNameTooltip extends Overlay
{
	@Inject
	private TooltipManager tooltipManager;

	@Inject
	private ItemSpriteManager itemSpriteManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private Client client;

	@Inject
	private TempleOSRSConfig config;

	private final Pattern itemIdPattern = Pattern.compile("<img=(\\d+)>(x\\d+(,\\s)?)");

	private final Pattern prefixPattern = Pattern.compile("([\\w\\s'()]+ \\(\\d+/\\d+\\): )");

	@Inject
	private ChatItemNameTooltip()
	{
		setPosition(OverlayPosition.TOOLTIP);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(PRIORITY_MED);
	}

	/**
	 * @param parent  The parent widget
	 * @param message The message widget
	 * @return true if the message is currently visible on the screen, else false.
	 */
	private boolean isMessageVisible(final Widget parent, final Widget message)
	{
		final int childNorthCoordinate = message.getRelativeY();
		final int childSouthCoordinate = childNorthCoordinate + message.getHeight();

		final int currentScrollNorthCoordinate = parent.getScrollY();
		final int currentScrollSouthCoordinate = currentScrollNorthCoordinate + parent.getHeight();

		if (childSouthCoordinate < currentScrollNorthCoordinate)
		{
			// The end of the message has been scrolled past
			return false;
		}

		// Returns true if the start of the message is currently visible, else false
		return childNorthCoordinate <= currentScrollSouthCoordinate;
	}

	/**
	 * Parses a chat message for its containing item icons, and map them to a matrix of rows & icon bounds.
	 *
	 * @param messageWidget The matched !col command message widget
	 * @param messagePrefix The text matching the command prefix, e.g. "Kree'arra (1/9): "
	 * @return A list of rows containing the icon bounds in that row
	 */
	private List<Map<Integer, Rectangle>> mapItemIcons(
		final Widget messageWidget,
		final String messagePrefix
	)
	{
		final Matcher iconIdMatcher = itemIdPattern.matcher(messageWidget.getText());
		final List<Map<Integer, Rectangle>> itemRows = new ArrayList<>(List.of(new HashMap<>()));

		final int textOffset = messageWidget.getFont().getTextWidth(messagePrefix);
		final int nbspWidth = messageWidget.getFont().getTextWidth(" ");

		final Rectangle chatMessageBounds = messageWidget.getBounds();
		final int chatMessageEastCoordinate = chatMessageBounds.x + chatMessageBounds.width;

		final AtomicInteger coordsX = new AtomicInteger(chatMessageBounds.x + textOffset);
		final AtomicInteger coordsY = new AtomicInteger(chatMessageBounds.y);
		final AtomicInteger row = new AtomicInteger(0);

		while (iconIdMatcher.find())
		{
			final int iconId = Integer.parseInt(iconIdMatcher.group(1));
			final String itemText = iconIdMatcher.group(2);
			final int itemId = itemSpriteManager.getSpriteItemIds().get(iconId);

			final int textWidth = ItemSpriteManager.SPRITE_WIDTH + messageWidget.getFont().getTextWidth(itemText);

			// The line heights differ between the chatbox and private messages,
			// which causes the tooltips to become misaligned if not dynamically calculated.
			final int messageLineHeight = messageWidget.getLineHeight();

			// The game ignores whitespace when rendering text at the edge of the chatbox,
			// hence it is subtracted in the calculation.
			// Otherwise, the icon coordinates can deviate wildly in longer lists.
			if (coordsX.get() + textWidth - nbspWidth > chatMessageEastCoordinate)
			{
				// If the new item would escape the bounds, move it onto a new line
				coordsX.set(chatMessageBounds.x);
				coordsY.addAndGet(messageLineHeight);

				itemRows.add(row.addAndGet(1), new HashMap<>());
			}

			final Rectangle itemCoordinates = new Rectangle(
				coordsX.get(),
				coordsY.get(),
				ItemSpriteManager.SPRITE_WIDTH,
				messageLineHeight
			);

			coordsX.addAndGet(textWidth);

			itemRows.get(row.get()).put(itemId, itemCoordinates);
		}

		return itemRows;
	}

	/**
	 * When a !col command chat message row is hovered,
	 * check to see if an item icon is being hovered and return its item name.
	 *
	 * @param iconCoordinates A map of icons in the currently hovered chat message row
	 * @param mouseX          The current X coordinate of the mouse
	 * @return The item name of the currently hovered icon, if found
	 */
	@Nullable()
	private String findHoveredItemName(final Map<Integer, Rectangle> iconCoordinates, final int mouseX)
	{
		for (Map.Entry<Integer, Rectangle> iconEntry : iconCoordinates.entrySet())
		{
			final Rectangle iconBounds = iconEntry.getValue();

			final int iconWestCoordinate = iconBounds.x;
			final int iconEastCoordinate = iconWestCoordinate + iconBounds.width;

			// Y coordinate bounds are done by filtering the rows based on the icon height
			// All we need to do here is check whether the X coordinate is hovered
			if (iconWestCoordinate <= mouseX && iconEastCoordinate >= mouseX)
			{
				final int hoveredItemId = iconEntry.getKey();

				return itemManager.getItemComposition(hoveredItemId).getName();
			}
		}

		return null;
	}

	/**
	 * Iterates through a list of chat messages, adding tooltip listeners to matching !col commands
	 *
	 * @param chatMessages     The chat messages from the interface widget
	 * @param messageContainer The interface widget that should be used to determine bounds
	 * @param mousePosition    The current mouse position
	 */
	private void addTooltipToChatMessages(
		final Widget[] chatMessages,
		final Widget messageContainer,
		final Point mousePosition
	)
	{
		for (Widget chatMessage : chatMessages)
		{
			final String messageText = chatMessage.getText();

			if (messageText.isEmpty())
			{
				continue;
			}

			final String strippedMessageText = Text.removeTags(messageText);
			final Matcher prefixMatcher = prefixPattern.matcher(strippedMessageText);

			if (
				prefixMatcher.find() &&
					itemIdPattern.matcher(messageText).find() &&
					isMessageVisible(messageContainer, chatMessage)
			)
			{
				final Rectangle bounds = chatMessage.getBounds();

				if (!bounds.contains(mousePosition.getX(), mousePosition.getY()))
				{
					continue;
				}

				final String messagePrefix = prefixMatcher.group();
				final List<Map<Integer, Rectangle>> itemIconRows = mapItemIcons(chatMessage, messagePrefix);
				final int hoveredRow = Math.floorDiv((int) (mousePosition.getY() - bounds.getY()), chatMessage.getLineHeight());
				final String hoveredItemName = findHoveredItemName(itemIconRows.get(hoveredRow), mousePosition.getX());

				if (hoveredItemName != null)
				{
					tooltipManager.add(new Tooltip(hoveredItemName));
				}
			}
		}
	}

	@Override
	public Dimension render(final Graphics2D graphics)
	{
		final Point mousePosition = client.getMouseCanvasPosition();
		final Widget chatBoxWidget = client.getWidget(InterfaceID.Chatbox.UNIVERSE);
		final Widget chatBoxScrollAreaWidget = client.getWidget(InterfaceID.Chatbox.SCROLLAREA);
		final Widget privateChatWidget = client.getWidget(InterfaceID.PmChat.CONTAINER);

		if (
			chatBoxWidget == null ||
				chatBoxScrollAreaWidget == null ||
				privateChatWidget == null ||
				(
					!chatBoxScrollAreaWidget.contains(mousePosition) &&
						!privateChatWidget.contains(mousePosition)
				)
		)
		{
			return null;
		}

		final Widget[] chatMessages = chatBoxScrollAreaWidget.getChildren();
		final Widget[] privateChatMessages = privateChatWidget.getChildren();

		if (chatMessages != null && chatBoxScrollAreaWidget.contains(mousePosition))
		{
			addTooltipToChatMessages(chatMessages, chatBoxScrollAreaWidget, mousePosition);
		}

		if (
			privateChatMessages != null &&
				privateChatWidget.contains(mousePosition) &&
				!chatBoxWidget.contains(mousePosition)
		)
		{
			addTooltipToChatMessages(privateChatMessages, privateChatWidget, mousePosition);
		}

		return null;
	}
}
