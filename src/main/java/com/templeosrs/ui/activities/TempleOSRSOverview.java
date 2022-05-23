package com.templeosrs.ui.activities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class TempleOSRSOverview extends JPanel
{
	public final JPanel playerOverview;

	public final TempleOSRSOverviewSection EXP;

	public final TempleOSRSOverviewSection EHP;

	public final TempleOSRSOverviewSection EHB;

	public TempleOSRSOverview()
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel playerInfoLabel = new JLabel("Overview");
		playerInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		playerInfoLabel.setBorder(new EmptyBorder(5, 5, 0, 0));
		playerInfoLabel.setFont(FontManager.getRunescapeBoldFont());
		playerInfoLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		layoutPanel.add(playerInfoLabel);

		playerOverview = new JPanel();
		playerOverview.setBorder(new EmptyBorder(5, 5, 5, 5));
		playerOverview.setLayout(new GridLayout(3, 1, 0, 2));

		EHP = new TempleOSRSOverviewSection("Ehp", "Efficient Hours Played", ColorScheme.DARKER_GRAY_COLOR);
		EHB = new TempleOSRSOverviewSection("Ehb", "Efficient Hours Bossed", ColorScheme.DARK_GRAY_HOVER_COLOR);
		EXP = new TempleOSRSOverviewSection("Xp", "Total Experience", ColorScheme.DARKER_GRAY_COLOR);

		playerOverview.add(EHP);
		playerOverview.add(EHB);
		playerOverview.add(EXP);

		layoutPanel.add(playerOverview, BorderLayout.SOUTH);
		add(layoutPanel);
	}

	public void reset()
	{
		EHP.reset();
		EHB.reset();
		EXP.reset();
	}
}
