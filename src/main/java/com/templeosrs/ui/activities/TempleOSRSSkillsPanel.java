/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * Copyright (c) 2019, Bram91 <https://github.com/bram91>
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

package com.templeosrs.ui.activities;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.NameAutocompleter;
import com.templeosrs.util.TempleOSRSPlayer;
import static com.templeosrs.util.TempleOSRSService.HOST;
import static com.templeosrs.util.TempleOSRSService.PLAYER_PAGE;
import static com.templeosrs.util.TempleOSRSService.fetchUserGainsAsync;
import com.templeosrs.util.playerinfo.TempleOSRSData;
import com.templeosrs.util.playerinfo.TempleOSRSSkill;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.Player;
import static net.runelite.client.RuneLite.SCREENSHOT_DIR;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class TempleOSRSSkillsPanel extends PluginPanel
{
	private static final String PATH = SCREENSHOT_DIR + File.separator + "TempleOSRS" + File.separator;

	private static final Map<String, String> TIMES = Stream.of(new String[][]{
		{"Day", "1day"},
		{"Week", "1week"},
		{"Month", "31day"},
		{"Six Months", "186days"},
		{"Year", "365days"},
		{"All Time", "alltime"},
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	public static IconTextField playerLookup;

	private final Client client;

	private final NameAutocompleter nameAutocompleter;

	private final TempleOSRSActivityPanel skills;

	private final TempleOSRSActivityPanel bosses;

	private final TempleOSRSOverview overview;

	private JButton searchButton;

	private JButton profileButton;

	@Inject
	public TempleOSRSSkillsPanel(Client client, NameAutocompleter nameAutocompleter)
	{
		this.client = client;
		this.nameAutocompleter = nameAutocompleter;

		skills = new TempleOSRSActivityPanel(HiscoreSkillType.SKILL);
		bosses = new TempleOSRSActivityPanel(HiscoreSkillType.BOSS);

		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel fetchPlayer = new JPanel();
		fetchPlayer.setLayout(new BoxLayout(fetchPlayer, BoxLayout.Y_AXIS));
		fetchPlayer.setBorder(new EmptyBorder(5, 5, 0, 5));
		fetchPlayer.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		playerLookup = buildTextField();
		fetchPlayer.add(playerLookup);

		JPanel buttons = buildFetchButtons();
		fetchPlayer.add(buttons);

		TempleOSRSTimeSelection timeSelection = new TempleOSRSTimeSelection(this);
		fetchPlayer.add(timeSelection);

		overview = new TempleOSRSOverview();

		JPanel display = new JPanel();
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);

		MaterialTab skillsTab = new MaterialTab("Skills", tabGroup, skills);
		MaterialTab bossesTab = new MaterialTab("Bosses", tabGroup, bosses);

		tabGroup.addTab(skillsTab);
		tabGroup.addTab(bossesTab);

		tabGroup.select(skillsTab);

		layoutPanel.add(fetchPlayer);
		layoutPanel.add(overview);
		layoutPanel.add(tabGroup);
		layoutPanel.add(display);
		layoutPanel.add(buildScreenshots());

		add(layoutPanel);

		addInputKeyListener(this.nameAutocompleter);
	}

	@Override
	public void onActivate()
	{
		super.onActivate();
		playerLookup.requestFocusInWindow();
	}

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
		buttonsLayout.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		searchButton = createNewButton("Search", "Search for player profile");
		searchButton.addActionListener(e -> fetchUser());

		profileButton = createNewButton("Open Page", "Opens TempleOSRS player page");
		profileButton.addActionListener(e -> open());

		buttonsLayout.add(searchButton);
		buttonsLayout.add(profileButton);

		return buttonsLayout;
	}

	private JButton createNewButton(String text, String tooltip)
	{
		JButton newButton = new JButton();

		newButton.setFont(FontManager.getRunescapeFont());
		newButton.setText(text);
		newButton.setToolTipText(tooltip);
		newButton.setForeground(ColorScheme.GRAND_EXCHANGE_LIMIT);

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

	private JPanel buildScreenshots()
	{
		JPanel saveLayout = new JPanel(new BorderLayout());
		saveLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveLayout.setOpaque(false);
		JPopupMenu menu = new JPopupMenu();

		JMenuItem takeScreenshot = new JMenuItem();
		takeScreenshot.setText("Take Screenshot of current view...");
		takeScreenshot.addActionListener(e -> screenshot(this));
		menu.add(takeScreenshot);

		JMenuItem openFolder = new JMenuItem();
		openFolder.setText("Open screenshot folder...");
		openFolder.addActionListener(e -> {
			if (SCREENSHOT_DIR.exists() || SCREENSHOT_DIR.mkdirs())
			{
				LinkBrowser.open(SCREENSHOT_DIR.getAbsolutePath());
			}
		});
		menu.add(openFolder);

		JButton screenshotButton = new JButton();
		screenshotButton.setBorder(new EmptyBorder(5, 5, 5, 5));
		screenshotButton.setIcon(new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "save.png")));
		screenshotButton.setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		screenshotButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				menu.show(screenshotButton, e.getX(), e.getY());
			}
		});
		saveLayout.add(screenshotButton, BorderLayout.WEST);

		return saveLayout;
	}

	public void fetchUser(String username)
	{
		playerLookup.setText(username);
		fetchUser();
	}

	public void fetchUser()
	{
		final String username = format(playerLookup.getText());

		if (Strings.isNullOrEmpty(username))
		{
			return;
		}

		if (username.length() > 12)
		{
			error();
			return;
		}

		loading();

		reset();

		String period = TIMES.get(String.valueOf(TempleOSRSTimeSelection.jComboBox.getSelectedItem()));

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

	private void rebuild(String username, TempleOSRSPlayer result, Throwable err)
	{
		if (!format(playerLookup.getText()).equals(username))
		{
			completed();
			return;
		}

		if (Objects.isNull(result) || Objects.nonNull(err) || result.error)
		{
			error();
			return;
		}
		rebuild(result);
	}

	private void rebuild(TempleOSRSPlayer result)
	{
		skills.update(result);
		bosses.update(result);
		rebuildConstants(result);

		completed();
	}

	private void rebuildConstants(TempleOSRSPlayer result)
	{
		TempleOSRSData bossingData = result.playerBossingData.data;
		TempleOSRSData skillsData = result.playerSkillsData.data;

		TempleOSRSSkill ehbData = bossingData.table.get("Ehb");
		TempleOSRSSkill ehpData = skillsData.table.get("Ehp");
		TempleOSRSSkill xpData = skillsData.table.get("Overall");

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

		overview.EHP.update((long) ehpRankTotal, (long) ehpTotal);
		overview.EHB.update((long) ehbRankTotal, (long) ehbTotal);
		overview.EXP.update((long) xpRankTotal, (long) xpTotal);

		bosses.update((long) ehbRankGain, ehbGain);
		skills.update((long) ehpRankGain, ehpGain);
	}

	private void reset()
	{
		skills.reset();
		bosses.reset();
		overview.reset();
	}

	private void error()
	{
		searchButton.setEnabled(true);
		profileButton.setEnabled(true);
		playerLookup.setIcon(IconTextField.Icon.ERROR);
		playerLookup.setEditable(true);
	}

	private void loading()
	{
		searchButton.setEnabled(false);
		profileButton.setEnabled(false);
		playerLookup.setIcon(IconTextField.Icon.LOADING);
		playerLookup.setEditable(false);
	}

	private void completed()
	{
		searchButton.setEnabled(true);
		profileButton.setEnabled(true);
		playerLookup.setIcon(IconTextField.Icon.SEARCH);
		playerLookup.setEditable(true);
	}

	private void open()
	{
		String username = format(playerLookup.getText());
		if (Strings.isNullOrEmpty(username))
		{
			return;
		}
		else if (username.length() > 12)
		{
			error();
			return;
		}

		loading();

		String playerPageURL = HOST + PLAYER_PAGE + username;
		SwingUtilities.invokeLater(() -> LinkBrowser.browse(playerPageURL));

		completed();
	}

	private void screenshot(JPanel panel)
	{
		final String username = format(playerLookup.getText());
		String timestamp = String.valueOf(Instant.now().getEpochSecond());

		File directory = new File(PATH);
		if (directory.exists() || directory.mkdirs())
		{
			BufferedImage img = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_RGB);
			panel.paint(img.createGraphics());
			File imageFile = new File(PATH + username + timestamp + ".png");
			try
			{
				if (imageFile.createNewFile())
				{
					ImageIO.write(img, "png", imageFile);
				}
			}
			catch (Exception ignored)
			{
			}
		}
	}

	private void addInputKeyListener(KeyListener l)
	{
		playerLookup.addKeyListener(l);
	}

	private void removeInputKeyListener(KeyListener l)
	{
		playerLookup.removeKeyListener(l);
	}

	private String format(String text)
	{
		String formatted = text.replaceAll("\\s+", "+");
		return formatted.replace('\u00A0', '+');
	}
}

