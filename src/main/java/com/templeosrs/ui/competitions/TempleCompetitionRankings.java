package com.templeosrs.ui.competitions;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.comp.TempleCompetitionParticipant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleCompetitionRankings extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.DARKER_GRAY_COLOR};

	public int i = 0;

	TempleCompetitionRankings(TempleOSRSPlugin plugin, List<TempleCompetitionParticipant> participantList)
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, -2, 0, -2));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel compRankings = new JPanel();
		compRankings.setLayout(new GridLayout(0, 1));

		/* create header-bar for competitions (Name, Total) */
		JPanel compHeader = new JPanel();
		compHeader.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		compHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		compHeader.setPreferredSize(new Dimension(PANEL_WIDTH, 20));
		compHeader.setLayout(new GridLayout(0, 2));

		JLabel label = new JLabel("Name");
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		label.setFont(FontManager.getRunescapeSmallFont());
		compHeader.add(label);

		JLabel gain = new JLabel("Total");
		gain.setBorder(new EmptyBorder(0, 5, 0, 0));
		gain.setFont(FontManager.getRunescapeSmallFont());
		compHeader.add(gain);

		/* add header to rankings-panel */
		compRankings.add(compHeader);

		/* for each player in participant-list */
		for (TempleCompetitionParticipant player : participantList)
		{
			/* if participant has some skill-gain for competition */
			if (Objects.nonNull(player.xpGained) && player.xpGained > 0)
			{
				/* create and add new row for participant */
				TempleCompetitionRow row = new TempleCompetitionRow(plugin, player, i + 1, COLORS[i % 2]);
				compRankings.add(row);
				i++;
			}
		}

		/* add participant-list to layout */
		layoutPanel.add(compRankings, BorderLayout.SOUTH);

		/* create custom border */
		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), "Competition Rankings");
		custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
		custom.setTitleJustification(TitledBorder.CENTER);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		/* if participant-count is too large -> add scroll-pane and set preferred dimensions */
		if (i > 15)
		{
			setPreferredSize(new Dimension(PANEL_WIDTH, 425));

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
