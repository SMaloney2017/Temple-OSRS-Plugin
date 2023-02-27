package com.templeosrs.ui.competitions;

import com.google.common.base.Strings;
import com.templeosrs.TempleOSRSConfig;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.Text;

public class TempleCompetitionWatchlist extends JPanel
{
	public static JComboBox<String> jComboBox;
	private final TempleOSRSConfig config;

	public TempleCompetitionWatchlist(TempleOSRSConfig config, TempleCompetitions panel)
	{
		this.config = config;

		String[] competitions = new String[Text.fromCSV(config.getCompetitionWatchlist()).size()];

		for (int i = 0; i < Text.fromCSV(config.getCompetitionWatchlist()).size(); i++)
		{
			competitions[i] = Text.fromCSV(config.getCompetitionWatchlist()).get(i);
		}

		setLayout(new BorderLayout());
		setOpaque(false);

		setMinimumSize(new Dimension(PANEL_WIDTH, 20));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		jComboBox = new JComboBox<>(competitions);
		jComboBox.addActionListener(e -> {
			panel.lookup.setText((String) jComboBox.getSelectedItem());
			panel.fetchCompetition();
		});
		jComboBox.setSelectedItem(config.getDefaultComp());

		add(jComboBox);
	}

	void addWatchlistItem(String competitionId)
	{
		if (Strings.isNullOrEmpty(competitionId))
		{
			return;
		}

		Set<String> watchlist = new LinkedHashSet<>(Text.fromCSV(config.getCompetitionWatchlist()));
		if (!watchlist.contains(competitionId))
		{
			watchlist.add(competitionId);
			config.setCompetitionWatchlist(Text.toCSV(watchlist));
		}
	}

	void removeWatchlistItem(String competitionId)
	{
		if (Strings.isNullOrEmpty(competitionId))
		{
			return;
		}

		Set<String> watchlist = new LinkedHashSet<>(Text.fromCSV(config.getCompetitionWatchlist()));
		if (watchlist.contains(competitionId))
		{
			watchlist.remove(competitionId);
			config.setCompetitionWatchlist(Text.toCSV(watchlist));
		}
	}
}
