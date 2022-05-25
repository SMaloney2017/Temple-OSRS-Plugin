package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleClanMembers extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.DARKER_GRAY_COLOR};

	public final JPanel clanMembers;

	public final JPanel layoutPanel;

	TempleClanMembers(TempleOSRSPlugin plugin, String heading, String[] members)
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		clanMembers = new JPanel();
		clanMembers.setLayout(new GridLayout(0, 1));

		for (int i = 0; i < members.length; i++)
		{
			TempleClanMember user = new TempleClanMember(plugin, members[i], COLORS[i % 2]);
			clanMembers.add(user);
		}

		layoutPanel.add(clanMembers, BorderLayout.SOUTH);

		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), heading);
		custom.setTitleColor(ColorScheme.PROGRESS_COMPLETE_COLOR);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		if (members.length > 12)
		{
			setPreferredSize(new Dimension(PANEL_WIDTH, 300));

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
