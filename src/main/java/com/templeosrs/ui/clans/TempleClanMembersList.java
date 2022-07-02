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
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleClanMembersList extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.DARKER_GRAY_COLOR};

	TempleClanMembersList(TempleOSRSPlugin plugin, String heading, String[] members)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel clanMembers = new JPanel();
		clanMembers.setLayout(new GridLayout(0, 1));

		/* for each member in members-list */
		for (int i = 0; i < members.length; i++)
		{
			/* create a new member-row and add to clan-members list */
			TempleClanMember user = new TempleClanMember(plugin, members[i], COLORS[i % 2]);
			clanMembers.add(user);
		}

		/* add clan-members list to main layout */
		layoutPanel.add(clanMembers, BorderLayout.SOUTH);

		/* create custom border, add to (layout or scroll) */
		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), heading);
		custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
		custom.setTitleJustification(TitledBorder.CENTER);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		/* if list is too large -> add scroll-pane and set preferred dimensions */
		if (members.length > 10)
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
