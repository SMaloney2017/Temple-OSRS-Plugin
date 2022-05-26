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
import com.templeosrs.util.NameAutocompleter;
import static com.templeosrs.util.TempleService.HOST;
import static com.templeosrs.util.TempleService.PLAYER_PAGE;
import static com.templeosrs.util.TempleService.fetchUserGainsAsync;
import com.templeosrs.util.player.TemplePlayer;
import com.templeosrs.util.player.TemplePlayerData;
import com.templeosrs.util.player.TemplePlayerSkill;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.LinkBrowser;

public class TempleRanks extends PluginPanel
{
	private static final Map<String, String> TIMES = Stream.of(new String[][]{
		{"Day", "1day"},
		{"Week", "1week"},
		{"Month", "31day"},
		{"Six Months", "186days"},
		{"Year", "365days"},
		{"All Time", "alltime"},
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	public final IconTextField playerLookup;

	private final Client client;

	private final NameAutocompleter nameAutocompleter;

	private final TempleActivity skills;

	private final TempleActivity bosses;

	private final TempleRanksOverview overview;

	private JButton searchButton;

	private JButton profileButton;

	@Inject
	public TempleRanks(Client client, NameAutocompleter nameAutocompleter)
	{
		this.client = client;
		this.nameAutocompleter = nameAutocompleter;

		skills = new TempleActivity(HiscoreSkillType.SKILL);
		bosses = new TempleActivity(HiscoreSkillType.BOSS);

		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel fetchLayout = new JPanel();
		fetchLayout.setLayout(new BoxLayout(fetchLayout, BoxLayout.Y_AXIS));
		fetchLayout.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		fetchLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* build and add search-text-field */
		playerLookup = buildTextField();
		fetchLayout.add(playerLookup);

		/* build and add search-buttons */
		JPanel buttons = buildFetchButtons();
		fetchLayout.add(buttons);

		/* build and add duration selection */
		TempleRanksDuration timeSelection = new TempleRanksDuration(this);
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
				if (e.getClickCount() != 2)
				{
					return;
				}
				if (client == null)
				{
					return;
				}

				Player localPlayer = client.getLocalPlayer();

				if (localPlayer != null)
				{
					fetchUser(localPlayer.getName());
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

	private JPanel buildFetchButtons()
	{
		JPanel buttonsLayout = new JPanel();
		buttonsLayout.setLayout(new FlowLayout());
		buttonsLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		searchButton = createNewButton("Search", "Search for player profile");
		searchButton.addActionListener(e -> fetchUser());

		profileButton = createNewButton("Open Page", "Opens TempleOSRS player page");
		profileButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(profileButton);

		return buttonsLayout;
	}

	public JButton createNewButton(String text, String tooltip)
	{
		JButton newButton = new JButton();

		newButton.setFont(FontManager.getRunescapeFont());
		newButton.setText(text);
		newButton.setToolTipText(tooltip);
		newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);

		/* add hover mouse-listener for buttons */
		newButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				newButton.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
			}

			public void mouseExited(MouseEvent evt)
			{
				newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);
			}
		});

		return newButton;
	}

	/* search-text-field double-click -> fetch local player */
	public void fetchUser(String username)
	{
		playerLookup.setText(username);
		fetchUser();
	}

	/* fetch player from search-text-field */
	public void fetchUser()
	{
		final String username = format(playerLookup.getText());

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

		String period = TIMES.get(String.valueOf(TempleRanksDuration.jComboBox.getSelectedItem()));

		/* create separate thread for completing player-fetch/ panel rebuilds,
		 *  try to fetch player gains,
		 *  when fetching completes, rebuild panel
		 *  if exception, set error status
		 */
		new Thread(() -> {
			try
			{
				fetchUserGainsAsync(username, period).whenCompleteAsync((result, err) -> rebuild(username, result, err));
			}
			catch (Exception ignored)
			{
				error();
			}
		}).start();
	}

	private void rebuild(String username, TemplePlayer result, Throwable err)
	{
		/* search-text-field has changed since start of fetching player data */
		if (!format(playerLookup.getText()).equals(username))
		{
			completed();
			return;
		}

		/* result is null, error is not null, or error response */
		if (Objects.isNull(result) || Objects.nonNull(err) || result.error)
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
		String username = format(playerLookup.getText());
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

		String playerPageURL = HOST + PLAYER_PAGE + username;
		SwingUtilities.invokeLater(() -> LinkBrowser.browse(playerPageURL));

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
		searchButton.setEnabled(true);
		profileButton.setEnabled(true);
		playerLookup.setIcon(IconTextField.Icon.ERROR);
		playerLookup.setEditable(true);
	}

	/* set fields for loading status */
	private void loading()
	{
		searchButton.setEnabled(false);
		profileButton.setEnabled(false);
		playerLookup.setIcon(IconTextField.Icon.LOADING);
		playerLookup.setEditable(false);
	}

	/* set fields for completed status */
	private void completed()
	{
		searchButton.setEnabled(true);
		profileButton.setEnabled(true);
		playerLookup.setIcon(IconTextField.Icon.SEARCH);
		playerLookup.setEditable(true);
	}

	private void addInputKeyListener(KeyListener l)
	{
		playerLookup.addKeyListener(l);
	}

	private void removeInputKeyListener(KeyListener l)
	{
		playerLookup.removeKeyListener(l);
	}

	/* format username to be accepted by Temple API */
	private String format(String text)
	{
		String formatted = text.replaceAll("\\s+", "+");
		return formatted.replace('\u00A0', '+');
	}
}

