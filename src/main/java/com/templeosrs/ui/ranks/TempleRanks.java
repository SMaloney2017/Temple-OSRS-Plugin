/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2020, dekvall
 * Copyright (c) 2021, Rorro
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

package com.templeosrs.ui.ranks;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.NameAutocompleter;
import com.templeosrs.util.PlayerRanges;
import com.templeosrs.util.TempleService;
import com.templeosrs.util.player.TemplePlayer;
import com.templeosrs.util.player.TemplePlayerData;
import com.templeosrs.util.player.TemplePlayerSkill;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.api.Client;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import okhttp3.HttpUrl;

public class TempleRanks extends PluginPanel
{
	public final IconTextField lookup;

	private final Client client;

	private final TempleOSRSConfig config;

	private final NameAutocompleter nameAutocompleter;

	private final TempleActivity skills;

	private final TempleActivity bosses;

	private final TempleRanksOverview overview;

	private final TempleService service;

	@Inject
	public TempleRanks(TempleOSRSConfig config, Client client, TempleService templeService, NameAutocompleter nameAutocompleter)
	{
		this.client = client;
		this.config = config;
		this.nameAutocompleter = nameAutocompleter;
		this.service = templeService;

		skills = new TempleActivity(HiscoreSkillType.SKILL);
		bosses = new TempleActivity(HiscoreSkillType.BOSS);

		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		JPanel searchLayout = new JPanel();
		searchLayout.setLayout(new BoxLayout(searchLayout, BoxLayout.X_AXIS));
		searchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		lookup = buildTextField();
		searchLayout.add(lookup);

		JLabel actions = new JLabel();
		actions.setBorder(new EmptyBorder(0, 5, 0, 0));
		actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears.png")));
		actions.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (client == null)
				{
					return;
				}

				JPopupMenu menu = new JPopupMenu();
				JMenuItem fetchPlayerMenuItem = new JMenuItem();
				fetchPlayerMenuItem.setText("Search");
				fetchPlayerMenuItem.addActionListener(ev -> fetchUser());
				menu.add(fetchPlayerMenuItem);

				JMenuItem openPlayerPageMenuItem = new JMenuItem();
				openPlayerPageMenuItem.setText("Open TempleOSRS");
				openPlayerPageMenuItem.addActionListener(ev -> open());
				menu.add(openPlayerPageMenuItem);

				actions.add(menu);
				menu.show(actions, e.getX(), e.getY());
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears_light.png")));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				actions.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "gears.png")));
			}
		});

		searchLayout.add(actions);
		fetchLayout.add(searchLayout);

		/* build and add duration selection */
		TempleRanksDuration timeSelection = new TempleRanksDuration(config, this);
		fetchLayout.add(timeSelection);

		add(fetchLayout);

		/* build and add player overview */
		overview = new TempleRanksOverview();
		add(overview);

		/* material-tab-group for selecting view of Skills/ Bosses */
		JPanel display = new JPanel();
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);

		MaterialTab skillsTab = new MaterialTab("Skills", tabGroup, skills);
		MaterialTab bossesTab = new MaterialTab("Bosses", tabGroup, bosses);

		tabGroup.addTab(skillsTab);
		tabGroup.addTab(bossesTab);
		tabGroup.select(skillsTab);
		add(tabGroup);

		add(display);

		/* add key listener for player-search autocomplete */
		addInputKeyListener(this.nameAutocompleter);

		/* load default player on start-up */
		if (config.fetchDefaults())
		{
			if (!Strings.isNullOrEmpty(config.getDefaultPlayer()))
			{
				fetchUser(config.getDefaultPlayer());
			}
		}
	}

	/* remove key listener for player-search autocomplete */
	public void shutdown()
	{
		removeInputKeyListener(this.nameAutocompleter);
	}

	private IconTextField buildTextField()
	{
		IconTextField lookup = new IconTextField();

		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
		lookup.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		lookup.setBackground(ColorScheme.SCROLL_TRACK_COLOR);

		/* add action listeners for fetching user / fetching local player */
		lookup.addActionListener(e -> fetchUser());
		lookup.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (client == null)
				{
					return;
				}

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
				{
					String player = config.getDefaultPlayer();

					if (!Strings.isNullOrEmpty(player))
					{
						fetchUser(player);
					}
				}
			}
		});

		/* reset icons and panel on clear */
		lookup.addClearListener(() -> {
			completed();
			reset();
		});

		return lookup;
	}

	/* search-text-field double-click -> fetch local player */
	public void fetchUser(String username)
	{
		lookup.setText(username);
		fetchUser();
	}

	/* fetch player from search-text-field */
	public void fetchUser()
	{
		final String username = format(lookup.getText());

		if (Strings.isNullOrEmpty(username))
		{
			return;
		}

		/* maximum username length */
		if (username.length() > 12)
		{
			error();
			return;
		}

		loading();

		reset();

		String period = PlayerRanges.get(String.valueOf(TempleRanksDuration.jComboBox.getSelectedItem())).getRange();

		/* create separate thread for completing player-fetch/ panel rebuilds,
		 *  try to fetch player gains,
		 *  when fetching completes, rebuild panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				service.fetchUserGainsAsync(username, period).whenCompleteAsync((result, err) -> response(username, result, err));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				error();
			}
		}).start();
	}

	private void response(String username, TemplePlayer result, Throwable e)
	{
		/* search-text-field has changed since start of fetching player data */
		if (!format(lookup.getText()).equals(username))
		{
			completed();
			return;
		}

		/* result is null, error is not null, or error response */
		if (Objects.isNull(result) || Objects.nonNull(e) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	/* rebuild components */
	private void rebuild(TemplePlayer result)
	{
		skills.update(result);
		bosses.update(result);
		rebuildOverall(result);

		completed();
	}

	private void rebuildOverall(TemplePlayer result)
	{
		/* get player skills and bosses data from result */
		TemplePlayerData bossingData = result.playerBossesOverview.data;
		TemplePlayerData skillsData = result.playerSkillsOverview.data;

		TemplePlayerSkill ehbData = bossingData.table.get("Ehb");
		TemplePlayerSkill ehpData = skillsData.table.get("Ehp");
		TemplePlayerSkill xpData = skillsData.table.get("Overall");

		/* if not null, get overall totals, ranks, and gains for ehp/ehb/xp */
		double ehbRankTotal = Objects.nonNull(ehbData.rankTotal) ? ehbData.rankTotal : 0;
		double ehbTotal = Objects.nonNull(ehbData.xpTotal) ? ehbData.xpTotal : 0;

		double ehbRankGain = Objects.nonNull(ehbData.rank) ? ehbData.rank : 0;
		double ehbGain = Objects.nonNull(ehbData.xp) ? ehbData.xp : 0;

		double ehpRankTotal = Objects.nonNull(ehpData.rankTotal) ? ehpData.rankTotal : 0;
		double ehpTotal = Objects.nonNull(ehpData.xpTotal) ? ehpData.xpTotal : 0;

		double ehpRankGain = Objects.nonNull(ehpData.rank) ? ehpData.rank : 0;
		double ehpGain = Objects.nonNull(ehpData.xp) ? ehpData.xp : 0;

		double xpRankTotal = Objects.nonNull(xpData.rankTotal) ? xpData.rankTotal : 0;
		double xpTotal = Objects.nonNull(xpData.xpTotal) ? xpData.xpTotal : 0;

		/* update player-overview sections */
		overview.EHP.update((long) ehpRankTotal, (long) ehpTotal);
		overview.EHB.update((long) ehbRankTotal, (long) ehbTotal);
		overview.EXP.update((long) xpRankTotal, (long) xpTotal);

		/* update activity overall-row */
		bosses.update((long) ehbRankGain, ehbGain);
		skills.update((long) ehpRankGain, ehpGain);
	}

	private void open()
	{
		String username = format(lookup.getText());
		if (Strings.isNullOrEmpty(username))
		{
			return;
		}

		if (username.length() > 12)
		{
			error();
			return;
		}

		/* if valid username format, open temple player-profile */
		loading();

		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("player").addPathSegment("overview.php").addQueryParameter("player", username).build();

		SwingUtilities.invokeLater(() -> LinkBrowser.browse(url.toString()));

		completed();
	}

	/* reset all panels */
	private void reset()
	{
		skills.reset();
		bosses.reset();
		overview.reset();

		repaint();
		revalidate();
	}

	/* set fields for error status */
	private void error()
	{
		lookup.setIcon(IconTextField.Icon.ERROR);
		lookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		lookup.setIcon(IconTextField.Icon.LOADING);
		lookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		lookup.setIcon(IconTextField.Icon.SEARCH);
		lookup.setEditable(true);
	}

	private void addInputKeyListener(KeyListener l)
	{
		lookup.addKeyListener(l);
	}

	private void removeInputKeyListener(KeyListener l)
	{
		lookup.removeKeyListener(l);
	}

	/* format username to be accepted by Temple API */
	private String format(String text)
	{
		String formatted = text.replaceAll("\\s+", "+");
		return formatted.replace('\u00A0', '+');
	}
}

