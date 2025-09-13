package com.templeosrs.util.collections.chatcommands;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.IndexedSprite;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

@Slf4j
@Singleton
public class ItemSpriteManager
{
	public static final int SPRITE_WIDTH = 18;
	public static final int SPRITE_HEIGHT = 14;

	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	/**
	 * Maintain a map of item IDs to their respective index in the sprite list
	 */
	@Getter
	protected final Map<Integer, Integer> itemSpriteIndexes = new HashMap<>();

	/**
	 * Maintain a map of sprite indexes to their respective item IDs
	 */
	@Getter
	protected final Map<Integer, Integer> spriteItemIds = new HashMap<>();

	/**
	 * Maintain a list of previously seen item sprites to avoid loading them twice
	 */
	protected final Set<Integer> loadedItemIds = new HashSet<>();

	/**
	 * Loads the in-game icon sprites for a given item list, ready to be used in the chat message.
	 *
	 * @param itemIds The item list for which to load item sprites.
	 */
	public void loadItemSprites(List<Integer> itemIds)
	{
		// Starting with an empty list, we find which icons haven't previously been seen
		final List<Integer> newItems = new ArrayList<>();

		for (int itemId : itemIds)
		{
			if (!loadedItemIds.contains(itemId))
			{
				newItems.add(itemId);
				loadedItemIds.add(itemId);
			}
		}

		if (newItems.isEmpty())
		{
			return;
		}

		final IndexedSprite[] modIcons = client.getModIcons();

		assert modIcons != null;

		final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + itemIds.size());
		final int iconIndex = modIcons.length;

		client.setModIcons(newModIcons);

		int i = 0;

		for (int itemId : itemIds)
		{
			final AsyncBufferedImage img = itemManager.getImage(itemId);
			final int idx = iconIndex + i++;

			itemSpriteIndexes.put(itemId, idx);
			spriteItemIds.put(idx, itemId);

			img.onLoaded(() ->
			{
				final BufferedImage image = ImageUtil.resizeImage(img, SPRITE_WIDTH, SPRITE_HEIGHT);
				final IndexedSprite sprite = ImageUtil.getImageIndexedSprite(image, client);

				// mod icons array might be replaced in between when we assign it and the callback,
				// so fetch mod icons again
				client.getModIcons()[idx] = sprite;
			});
		}
	}
}
