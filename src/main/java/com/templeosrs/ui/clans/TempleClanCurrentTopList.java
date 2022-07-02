package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.CurrentTopRanges;
import com.templeosrs.util.clan.TempleClanCurrentTopPlayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleClanCurrentTopList extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.DARKER_GRAY_COLOR};

	TempleClanCurrentTopList(TempleOSRSPlugin plugin, Map<String, TempleClanCurrentTopPlayer> skill, CurrentTopRanges range)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel currentTop = new JPanel();
		currentTop.setLayout(new GridLayout(0, 1));

		/* create a list, if map is not null -> add map entries to list */
		ArrayList<TempleClanCurrentTopPlayer> players = new ArrayList<>();
		if (Objects.nonNull(skill))
		{
			players.addAll(skill.values());
		}

		/* for each member in the list of current-top players */
		for (int i = 0; i < players.size(); i++)
		{
			/* create a new member-row and add to current-top list */
			TempleClanCurrentTopPlayer player = players.get(i);
			TempleClanCurrentTopRow user = new TempleClanCurrentTopRow(plugin, i + 1, player.player, String.format("%.2f", player.xp), COLORS[i % 2]);
			currentTop.add(user);
		}

		/* add current-top list to main layout */
		layoutPanel.add(currentTop, BorderLayout.SOUTH);

		/* create custom border, add to (layout or scroll) */

		String title = "Current Top - " + range.getName();
		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), title);
		custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
		custom.setTitleJustification(TitledBorder.CENTER);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		/* if list is too large -> add scroll-pane and set preferred dimensions */
		if (players.size() > 10)
		{
			setPreferredSize(new Dimension(PANEL_WIDTH, 275));

			/* create and add scroll-pane */
			final JScrollPane scroll = new JScrollPane(layoutPanel);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			scroll.setBorder(custom);
			add(scroll);
		}
		else
		{
			layoutPanel.setBorder(custom);
			add(layoutPanel);
		}
	}
}
