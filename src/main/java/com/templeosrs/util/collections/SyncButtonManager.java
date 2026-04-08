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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import static java.lang.Math.round;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.FontID;
import net.runelite.api.MenuAction;
import net.runelite.api.SpriteID;
import net.runelite.api.annotations.Component;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetTextAlignment;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.config.RuneScapeProfileType;


@Slf4j
@Singleton
public class SyncButtonManager
{
	private static final int COLLECTION_LOG_SETUP = 7797;
	private static final int[] SPRITE_IDS_INACTIVE = {
		SpriteID.DIALOG_BACKGROUND,
		SpriteID.WORLD_MAP_BUTTON_METAL_CORNER_TOP_LEFT,
		SpriteID.WORLD_MAP_BUTTON_METAL_CORNER_TOP_RIGHT,
		SpriteID.WORLD_MAP_BUTTON_METAL_CORNER_BOTTOM_LEFT,
		SpriteID.WORLD_MAP_BUTTON_METAL_CORNER_BOTTOM_RIGHT,
		SpriteID.WORLD_MAP_BUTTON_EDGE_LEFT,
		SpriteID.WORLD_MAP_BUTTON_EDGE_TOP,
		SpriteID.WORLD_MAP_BUTTON_EDGE_RIGHT,
		SpriteID.WORLD_MAP_BUTTON_EDGE_BOTTOM,
	};

	private static final int[] SPRITE_IDS_ACTIVE = {
		SpriteID.RESIZEABLE_MODE_SIDE_PANEL_BACKGROUND,
		SpriteID.EQUIPMENT_BUTTON_METAL_CORNER_TOP_LEFT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_METAL_CORNER_TOP_RIGHT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_METAL_CORNER_BOTTOM_LEFT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_METAL_CORNER_BOTTOM_RIGHT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_EDGE_LEFT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_EDGE_TOP_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_EDGE_RIGHT_HOVERED,
		SpriteID.EQUIPMENT_BUTTON_EDGE_BOTTOM_HOVERED,
	};

	private static final int FONT_COLOUR_INACTIVE = 0xd6d6d6;
	private static final int FONT_COLOUR_ACTIVE = 0xffffff;
	private static final int CLOSE_BUTTON_OFFSET = 28;
	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_OFFSET = CLOSE_BUTTON_OFFSET + 5;
	private int lastAttemptedUpdate = -1;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;

	@Inject
	private CollectionLogManager collectionLogManager;

	@Getter
	@Setter
	private boolean fullSyncRequested = false;

	public void startUp()
	{
		eventBus.register(this);
		clientThread.invokeLater(() -> tryAddButton(this::onButtonClick));
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		clientThread.invokeLater(this::removeButton);
	}

	void tryAddButton(Runnable onClick)
	{
		for (Screen screen : Screen.values())
		{
			addButton(screen, onClick);
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (scriptPostFired.getScriptId() == COLLECTION_LOG_SETUP)
		{
			removeButton();
			addButton(Screen.COLLECTION_LOG, this::onButtonClick);
		}
	}

	void onButtonClick()
	{
		if (lastAttemptedUpdate != -1 && lastAttemptedUpdate + 50 > client.getTickCount())
		{
			client.addChatMessage(ChatMessageType.CONSOLE, "TempleOSRS", "Last update within 30 seconds. You can update again in " + round((lastAttemptedUpdate + 50 - client.getTickCount()) * 0.6) + " seconds.", "TempleOSRS");
			return;
		}

		lastAttemptedUpdate = client.getTickCount();

		setFullSyncRequested(true);

		// Clear the previously obtained item list to avoid duplicating items when counts change
		collectionLogManager.getObtainedCollectionLogItems().clear();

		client.menuAction(-1, 40697932, MenuAction.CC_OP, 1, -1, "Search", null);
		client.runScript(2240);
		client.addChatMessage(ChatMessageType.CONSOLE, "TempleOSRS", "Your collection log data is being sent to TempleOSRS...", "TempleOSRS");
	}

	void addButton(Screen screen, Runnable onClick)
	{
		RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);

		if (profileType.toString() != "STANDARD")
		{
			return;
		}

		Widget parent = client.getWidget(screen.getParentId());
		Widget searchButton = client.getWidget(screen.getSearchButtonId());
		Widget collectionLogContainer = client.getWidget(screen.getCollectionLogContainer());
		Widget[] containerChildren;
		Widget draggableTopbar;
		if (parent == null || searchButton == null || collectionLogContainer == null ||
			(containerChildren = collectionLogContainer.getChildren()) == null ||
			(draggableTopbar = containerChildren[0]) == null)
		{
			return;
		}

		final int w = BUTTON_WIDTH;
		final int h = searchButton.getOriginalHeight();
		final int x = BUTTON_OFFSET;
		final int y = searchButton.getOriginalY();
		final int cornerDim = 9;

		final Widget[] spriteWidgets = new Widget[10];

		spriteWidgets[0] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[0])
			.setPos(x, y)
			.setSize(w, h)
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setYPositionMode(searchButton.getYPositionMode());

		spriteWidgets[1] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[1])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(cornerDim, cornerDim)
			.setPos(x + (w - cornerDim), y);
		spriteWidgets[2] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[2])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(cornerDim, cornerDim)
			.setPos(x, y);
		spriteWidgets[3] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[3])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(cornerDim, cornerDim)
			.setPos(x + (w - cornerDim), y + h - cornerDim);
		spriteWidgets[4] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[4])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(cornerDim, cornerDim)
			.setPos(x, y + h - cornerDim);
		// Left and right edges
		int sideWidth = 9;
		int sideHeight = 4;
		spriteWidgets[5] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[5])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(sideWidth, sideHeight)
			.setPos(x + (w - sideWidth), y + cornerDim);
		spriteWidgets[7] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[7])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(sideWidth, sideHeight)
			.setPos(x, y + cornerDim);

		// Top and bottom edges
		int topWidth = 42;
		int topHeight = 9;
		spriteWidgets[6] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[6])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(topWidth, topHeight)
			.setPos(x + cornerDim, y);
		spriteWidgets[8] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SPRITE_IDS_INACTIVE[8])
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(topWidth, topHeight)
			.setPos(x + cornerDim, y + h - topHeight);
		// Refresh icon
		spriteWidgets[9] = parent.createChild(-1, WidgetType.GRAPHIC)
			.setSpriteId(SpriteID.UNKNOWN_WHITE_REFRESH_ARROWS)
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setSize(13, 13)
			.setPos(x + 6, y + 4);


		for (int i = 0; i < 10; i++)
		{
			spriteWidgets[i].revalidate();
		}

		final Widget text = parent.createChild(-1, WidgetType.TEXT)
			.setText("Temple")
			.setTextColor(FONT_COLOUR_INACTIVE)
			.setFontId(FontID.PLAIN_11)
			.setTextShadowed(true)
			.setXPositionMode(WidgetPositionMode.ABSOLUTE_RIGHT)
			.setXTextAlignment(WidgetTextAlignment.LEFT)
			.setYTextAlignment(WidgetTextAlignment.CENTER)
			.setPos(x - 8, y)
			.setSize(w, h)
			.setYPositionMode(searchButton.getYPositionMode());
		text.revalidate();

		// We'll give the text layer the listeners since it covers the whole area
		text.setHasListener(true);
		text.setOnMouseOverListener((JavaScriptCallback) ev ->
		{
			for (int i = 0; i <= 8; i++)
			{
				spriteWidgets[i].setSpriteId(SPRITE_IDS_ACTIVE[i]);
			}
			text.setTextColor(FONT_COLOUR_ACTIVE);
		});
		text.setOnMouseLeaveListener((JavaScriptCallback) ev ->
		{
			for (int i = 0; i <= 8; i++)
			{
				spriteWidgets[i].setSpriteId(SPRITE_IDS_INACTIVE[i]);
			}
			text.setTextColor(FONT_COLOUR_INACTIVE);
		});

		// Register a click listener
		text.setAction(0, "Sync your collection log with Temple");
		text.setOnOpListener((JavaScriptCallback) ev -> onClick.run());


		// Shrink the top bar to avoid overlapping the new button
		draggableTopbar.setOriginalWidth(draggableTopbar.getOriginalWidth() - (w + (x - CLOSE_BUTTON_OFFSET)));
		draggableTopbar.revalidate();

		// recompute locations / sizes on parent
		parent.revalidate();
	}

	void removeButton()
	{
		for (Screen screen : Screen.values())
		{
			Widget parent = client.getWidget(screen.getParentId());
			if (parent != null)
			{
				parent.deleteAllChildren();
				parent.revalidate();
			}
		}
	}

	@Getter
	@RequiredArgsConstructor
	enum Screen
	{
		// First number is col log container (inner) and second is search button id
		COLLECTION_LOG(40697944, 40697932, ComponentID.COLLECTION_LOG_CONTAINER),
		;

		@Getter(onMethod_ = @Component)
		private final int parentId;

		@Getter(onMethod_ = @Component)
		private final int searchButtonId;

		@Getter(onMethod_ = @Component)
		private final int collectionLogContainer;
	}
}