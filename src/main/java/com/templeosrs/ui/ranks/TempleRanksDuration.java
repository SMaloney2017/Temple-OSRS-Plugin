package com.templeosrs.ui.ranks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleRanksDuration extends JPanel
{
	private static final String[] OPTIONS = {
		"All Time", "Day", "Week", "Month", "Six Months", "Year"
	};

	public static JComboBox<String> jComboBox;

	public TempleRanksDuration(TempleRanks panel)
	{
		setLayout(new BorderLayout());
		setOpaque(false);

		setMinimumSize(new Dimension(PANEL_WIDTH, 20));
		setBorder(new EmptyBorder(0, 0, 5, 0));

		jComboBox = new JComboBox<>(OPTIONS);
		jComboBox.addActionListener(e -> panel.fetchUser());

		add(jComboBox);
	}
}
