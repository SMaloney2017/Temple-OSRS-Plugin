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

	private boolean isMessageVisible(final Widget parent, final Widget line)
	{
		final int childNorthCoordinate = line.getRelativeY();
		final int childSouthCoordinate = childNorthCoordinate + line.getHeight();

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

	private List<Map<Integer, Rectangle>> mapItemIcons(
		final Widget line,
		final String messagePrefix,
		final String message
	)
	{
		final Matcher iconIdMatcher = itemIdPattern.matcher(message);
		final List<Map<Integer, Rectangle>> itemRows = new ArrayList<>(List.of(new HashMap<>()));

		final int textOffset = line.getFont().getTextWidth(messagePrefix);
		final int nbspWidth = line.getFont().getTextWidth(" ");

		final Rectangle chatMessageBounds = line.getBounds();
		final int chatMessageEastCoordinate = chatMessageBounds.x + chatMessageBounds.width;

		final AtomicInteger coordsX = new AtomicInteger(chatMessageBounds.x + textOffset);
		final AtomicInteger coordsY = new AtomicInteger(chatMessageBounds.y);
		final AtomicInteger row = new AtomicInteger(0);

		while (iconIdMatcher.find())
		{
			final int iconId = Integer.parseInt(iconIdMatcher.group(1));
			final String itemText = iconIdMatcher.group(2);
			final int itemId = itemSpriteManager.getSpriteItemIds().get(iconId);

			final int textWidth = ItemSpriteManager.SPRITE_WIDTH + line.getFont().getTextWidth(itemText);

			// The game ignores whitespace when rendering text at the edge of the chatbox,
			// hence it is subtracted in the calculation.
			// Otherwise, the icon coordinates can deviate wildly in longer lists.
			if (coordsX.get() + textWidth - nbspWidth > chatMessageEastCoordinate)
			{
				// If the new item would escape the bounds, move it onto a new line
				coordsX.set(chatMessageBounds.x);
				coordsY.addAndGet(ItemSpriteManager.SPRITE_HEIGHT);

				itemRows.add(row.addAndGet(1), new HashMap<>());
			}

			final Rectangle itemCoordinates = new Rectangle(
				coordsX.get(),
				coordsY.get(),
				ItemSpriteManager.SPRITE_WIDTH,
				ItemSpriteManager.SPRITE_HEIGHT
			);

			coordsX.addAndGet(textWidth);

			itemRows.get(row.get()).put(itemId, itemCoordinates);
		}

		return itemRows;
	}

	@Nullable()
	private String findHoveredItemName(final Map<Integer, Rectangle> iconCoordinates, final Point mousePosition)
	{
		for (Map.Entry<Integer, Rectangle> iconEntry : iconCoordinates.entrySet())
		{
			if (iconEntry.getValue().contains(mousePosition.getX(), mousePosition.getY()))
			{
				final int hoveredItemId = iconEntry.getKey();

				return itemManager.getItemComposition(hoveredItemId).getName();
			}
		}

		return null;
	}

	@Override
	public Dimension render(final Graphics2D graphics)
	{
		final Point mousePosition = client.getMouseCanvasPosition();
		final Widget chatBoxScrollAreaWidget = client.getWidget(InterfaceID.Chatbox.SCROLLAREA);

		if (chatBoxScrollAreaWidget == null || !chatBoxScrollAreaWidget.contains(mousePosition))
		{
			return null;
		}

		final Widget[] chatMessages = chatBoxScrollAreaWidget.getChildren();

		if (chatMessages == null)
		{
			return null;
		}

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
					isMessageVisible(chatBoxScrollAreaWidget, chatMessage)
			)
			{
				final Rectangle bounds = chatMessage.getBounds();

				if (!bounds.contains(mousePosition.getX(), mousePosition.getY()))
				{
					continue;
				}

				final String messagePrefix = prefixMatcher.group();
				final List<Map<Integer, Rectangle>> itemIconRows = mapItemIcons(chatMessage, messagePrefix, messageText);
				final int hoveredRow = Math.floorDiv((int) (mousePosition.getY() - bounds.getY()), ItemSpriteManager.SPRITE_HEIGHT);
				final String hoveredItemName = findHoveredItemName(itemIconRows.get(hoveredRow), mousePosition);

				if (hoveredItemName != null)
				{
					tooltipManager.add(new Tooltip(hoveredItemName));
				}
			}
		}

		return null;
	}
}
