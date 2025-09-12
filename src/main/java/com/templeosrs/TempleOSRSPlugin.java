/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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

package com.templeosrs;

import com.google.inject.Provides;
import com.templeosrs.ui.TempleOSRSPanel;
import com.templeosrs.ui.clans.TempleClans;
import com.templeosrs.ui.competitions.TempleCompetitions;
import com.templeosrs.ui.ranks.TempleRanks;
import com.templeosrs.util.TempleService;
import com.templeosrs.util.collections.CollectionLogManager;
import com.templeosrs.util.collections.SyncButtonManager;
import com.templeosrs.util.collections.chatcommands.ChatItemNameTooltip;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.xpupdater.XpUpdaterConfig;
import net.runelite.client.plugins.xpupdater.XpUpdaterPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.*;

@PluginDependency(XpUpdaterPlugin.class)
@PluginDescriptor(name = "TempleOSRS", description = "A RuneLite plugin utilizing the TempleOSRS API.", tags = {"Temple", "ehp", "ehb"})
public class TempleOSRSPlugin extends Plugin {
    private static final String TEMPLE = "Temple";

    private static final int XP_THRESHOLD = 10000;

    private static NavigationButton navButton;

    public TempleRanks ranks;

    public TempleClans clans;

    public TempleCompetitions competitions;

    public TempleOSRSPanel panel;

    private long lastAccount;

    private boolean fetchXp;

    private long lastXp;

    @Inject
    private Client client;

    @Inject
    private Provider<MenuManager> menuManager;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private ClientToolbar clientToolbar;

    @Getter
    @Inject
    private TempleOSRSConfig config;

    @Inject
    private XpUpdaterConfig xpUpdaterConfig;

    @Inject
    private XpUpdaterPlugin xpUpdaterPlugin;

    @Inject
    private TempleService service;

    @Inject
    private SyncButtonManager syncButtonManager;

    @Inject
    private CollectionLogManager clogManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ChatItemNameTooltip chatItemNameTooltip;

    @Override
    protected void startUp() {
        fetchXp = true;

        lastAccount = -1L;

        ranks = injector.getInstance(TempleRanks.class);

        clans = injector.getInstance(TempleClans.class);

        competitions = injector.getInstance(TempleCompetitions.class);

        panel = new TempleOSRSPanel(ranks, clans, competitions);
        navButton = NavigationButton.builder().tooltip("TempleOSRS").icon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "skills/skill_icon_ehp.png")).priority(5).panel(panel).build();

        if (config.showSidebar()) {
            clientToolbar.addNavigation(navButton);
        }

        if (config.playerLookup() && client != null) {
            menuManager.get().addPlayerMenuItem(TEMPLE);
        }

        // Only display clog update button if enabled
        if (config.clogSyncButton()) {
            syncButtonManager.startUp();
        }

        clogManager.startUp();

		if (config.enableClogChatCommand() && config.enableClogChatCommandItemNameTooltip())
		{
			overlayManager.add(chatItemNameTooltip);
		}
    }

    @Override
    protected void shutDown() {
        clientToolbar.removeNavigation(navButton);

        if (client != null) {
            menuManager.get().removePlayerMenuItem(TEMPLE);
        }

        ranks.shutdown();

        clogManager.shutDown();

		if (config.enableClogChatCommand() && config.enableClogChatCommandItemNameTooltip())
		{
			overlayManager.remove(chatItemNameTooltip);
		}
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(TempleOSRSConfig.TEMPLE_OSRS_CONFIG_GROUP)) {
            if (client != null) {
                menuManager.get().removePlayerMenuItem(TEMPLE);
                if (config.playerLookup()) {
                    menuManager.get().addPlayerMenuItem(TEMPLE);
                }

                // Show/Hide plugin icon in sidebar
                if (config.showSidebar()) {
                    clientToolbar.addNavigation(navButton);
                } else {
                    clientToolbar.removeNavigation(navButton);
                }

                if (clans.clanAchievements != null) {
                    clans.remove(clans.clanAchievements);
                    if (config.displayClanAchievements()) {
                        clans.add(clans.clanAchievements);
                    }
                }

                if (clans.clanCurrentTop != null) {
                    clans.remove(clans.clanCurrentTop);
                    if (config.displayClanCurrentTop()) {
                        clans.add(clans.clanCurrentTop);
                    }
                }

                if (clans.clanMembers != null) {
                    clans.remove(clans.clanMembers);
                    if (config.displayClanMembers()) {
                        clans.add(clans.clanMembers);
                    }
                }

                // Collection Log Related
                if (config.clogSyncButton()) {
                    syncButtonManager.startUp();
                } else {
                    syncButtonManager.shutDown();
                }

				if (config.enableClogChatCommand() && config.enableClogChatCommandItemNameTooltip())
				{
					overlayManager.add(chatItemNameTooltip);
				}
				else
				{
					overlayManager.remove(chatItemNameTooltip);
				}

                clans.repaint();
                clans.revalidate();
            }

            competitions.rebuildWatchlist();
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if ((event.getType() != MenuAction.CC_OP.getId() && event.getType() != MenuAction.CC_OP_LOW_PRIORITY.getId()) || !config.playerLookup()) {
            return;
        }

        String username = Text.toJagexName(Text.removeTags(event.getTarget()).toLowerCase().trim());

        final String option = event.getOption();
        final int componentId = event.getActionParam1();
        final int groupId = WidgetUtil.componentToInterface(componentId);

        if (groupId == InterfaceID.FRIEND_LIST && option.equals("Delete")
                || groupId == InterfaceID.FRIENDS_CHAT && (option.equals("Add ignore") || option.equals("Remove friend"))
                || groupId == InterfaceID.CHATBOX && (option.equals("Add ignore") || option.equals("Message"))
                || groupId == InterfaceID.IGNORE_LIST && option.equals("Delete")
                || (componentId == ComponentID.CLAN_MEMBERS || componentId == ComponentID.CLAN_GUEST_MEMBERS) && (option.equals("Add ignore") || option.equals("Remove friend"))
                || groupId == InterfaceID.PRIVATE_CHAT && (option.equals("Add ignore") || option.equals("Message"))
                || groupId == InterfaceID.GROUP_IRON && (option.equals("Add friend") || option.equals("Remove friend") || option.equals("Remove ignore"))
        ) {
            client.createMenuEntry(-2).setOption(TEMPLE).setTarget(event.getTarget()).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).onClick(e -> fetchUser(username));
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.getMenuAction() == MenuAction.RUNELITE_PLAYER && event.getMenuOption().equals(TEMPLE)) {
            Player player = event.getMenuEntry().getPlayer();
            if (player == null) {
                return;
            }

            String username = player.getName();
            fetchUser(username);
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        if (fetchXp) {
            lastXp = client.getOverallExperience();
            fetchXp = false;
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        GameState state = gameStateChanged.getGameState();
        if (state == GameState.LOGGED_IN) {
            if (lastAccount != client.getAccountHash()) {
                lastAccount = client.getAccountHash();
                fetchXp = true;
            }
        } else if (state == GameState.LOGIN_SCREEN) {
            Player local = client.getLocalPlayer();
            if (local == null) {
                return;
            }

            long totalXp = client.getOverallExperience();
            String username = local.getName();

			/* Don't submit update if xp-threshold has not been reached or username is null
			   or config option for auto-update is disabled */
            if (Math.abs(totalXp - lastXp) > XP_THRESHOLD && username != null && config.autoUpdate()) {
                updateUser(lastAccount, username.replace(" ", "+"));
                lastXp = totalXp;
            }
        }
    }

    @Provides
    TempleOSRSConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TempleOSRSConfig.class);
    }

    public void fetchUser(String username) {
        SwingUtilities.invokeLater(() -> {
            clientToolbar.openPanel(navButton);

            /* select ranks-tab */
            panel.tabGroup.select(panel.ranksTab);
            ranks.fetchUser(username);
        });
    }

    public void updateUser(long accountHash, String username) {
        /* if XpUpdaterPlugin is disabled or XpUpdaterPlugin's config option for templeosrs is disabled */
        if (!pluginManager.isPluginEnabled(xpUpdaterPlugin) || !xpUpdaterConfig.templeosrs()) {
            new Thread(() -> {
                try {
                    service.addDatapointAsync(username, accountHash);
                } catch (Exception ignored) {

                }
            }).start();
        }
    }
}
