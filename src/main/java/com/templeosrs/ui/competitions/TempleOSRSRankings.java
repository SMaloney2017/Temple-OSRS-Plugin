package com.templeosrs.ui.competitions;

import com.templeosrs.util.compinfo.TempleOSRSCompParticipant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSRankings extends JPanel
{

	private static final Color[] COLORS = {ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARK_GRAY_HOVER_COLOR};

	public int i = 0;

	TempleOSRSRankings(List<TempleOSRSCompParticipant> participantList)
	{
		setLayout(new GridLayout(0, 1));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel competitionHeaderLabel = new JLabel("Competition Rankings");
		competitionHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		competitionHeaderLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
		competitionHeaderLabel.setFont(FontManager.getRunescapeBoldFont());
		competitionHeaderLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		layoutPanel.add(competitionHeaderLabel);

		if (participantList != null)
		{
			JPanel compRankings = new JPanel();
			compRankings.setLayout(new GridLayout(0, 1, 0, 2));

			for (TempleOSRSCompParticipant player : participantList)
			{
				if (Objects.nonNull(player.xpGained) && player.xpGained > 0)
				{
					TempleOSRSCompetitionRank row = new TempleOSRSCompetitionRank(player, COLORS[i % 2]);
					compRankings.add(row);
					i++;
				}
			}

			layoutPanel.add(compRankings, BorderLayout.SOUTH);

			if (compRankings.getComponentCount() > 10)
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
}
