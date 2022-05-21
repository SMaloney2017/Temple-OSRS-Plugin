package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSClanMembers extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARK_GRAY_HOVER_COLOR};

	public final JPanel clanMembers;

	public final JPanel layoutPanel;

	TempleOSRSClanMembers(TempleOSRSPlugin plugin, String heading, String[] members)
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel clanHeaderLabel = new JLabel(heading);
		clanHeaderLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
		clanHeaderLabel.setFont(FontManager.getRunescapeBoldFont());
		clanHeaderLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		JPanel infoSelection = new JPanel();
		infoSelection.setLayout(new BoxLayout(infoSelection, BoxLayout.Y_AXIS));
		infoSelection.add(clanHeaderLabel);

		layoutPanel.add(infoSelection);

		clanMembers = new JPanel();
		clanMembers.setLayout(new GridLayout(0, 1, 0, 2));

		for (int i = 0; i < members.length; i++)
		{
			TempleOSRSMember user = new TempleOSRSMember(plugin, members[i], COLORS[i % 2]);
			clanMembers.add(user);
		}

		layoutPanel.add(clanMembers, BorderLayout.SOUTH);

		if (members.length > 10)
		{
			setPreferredSize(new Dimension(PANEL_WIDTH, 250));

			final JScrollPane scroll = new JScrollPane(layoutPanel);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			add(scroll);
		}
		else
		{
			add(layoutPanel);
		}
	}
}
