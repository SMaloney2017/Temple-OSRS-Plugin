/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * Copyright (c) 2019, Bram91 <https://github.com/bram91>
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

package com.templeosrs.ui;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.ui.activities.TempleRanks;
import com.templeosrs.ui.clans.TempleClans;
import com.templeosrs.ui.competitions.TempleCompetitions;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import static net.runelite.client.RuneLite.RUNELITE_DIR;
import static net.runelite.client.RuneLite.SCREENSHOT_DIR;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class TempleOSRSPanel extends PluginPanel
{
	public static final String DEFAULT = "--";

	private static final String SCREENSHOTS = SCREENSHOT_DIR + File.separator + "Temple-Snapshots" + File.separator;

	private static final String FAVORITES = RUNELITE_DIR + File.separator + "Temple-Favorites" + File.separator;

	public final TempleRanks ranks;

	public final TempleClans groups;

	public final TempleCompetitions competitions;

	public final MaterialTabGroup tabGroup;

	public final MaterialTab skillsTab;

	public final MaterialTab groupsTab;

	public final MaterialTab competitionsTab;

	@Inject
	public TempleOSRSPanel(TempleRanks ranks, TempleClans clans, TempleCompetitions comps)
	{
		this.ranks = ranks;
		this.groups = clans;
		this.competitions = comps;

		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel display = new JPanel();
		tabGroup = new MaterialTabGroup(display);

		skillsTab = new MaterialTab("Ranks", tabGroup, ranks);
		groupsTab = new MaterialTab("Clans", tabGroup, groups);
		competitionsTab = new MaterialTab("Competitions", tabGroup, competitions);

		tabGroup.addTab(skillsTab);
		tabGroup.addTab(groupsTab);
		tabGroup.addTab(competitionsTab);

		tabGroup.select(skillsTab);

		layoutPanel.add(tabGroup);
		layoutPanel.add(display);
		layoutPanel.add(buildScreenshots());

		add(layoutPanel);
	}

	private JPanel buildScreenshots()
	{
		JPanel saveLayout = new JPanel(new BorderLayout());
		saveLayout.setBorder(new EmptyBorder(5, 5, 5, 5));
		saveLayout.setBackground(ColorScheme.DARKER_GRAY_COLOR);

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

	private void screenshot(JPanel panel)
	{
		String timestamp = String.valueOf(Instant.now().getEpochSecond());

		File directory = new File(SCREENSHOTS);
		if (directory.exists() || directory.mkdirs())
		{
			BufferedImage img = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_RGB);
			panel.paint(img.createGraphics());
			File imageFile = new File(SCREENSHOTS + timestamp + ".png");
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
}
