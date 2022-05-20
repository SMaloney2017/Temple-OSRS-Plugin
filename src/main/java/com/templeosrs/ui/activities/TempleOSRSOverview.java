package com.templeosrs.ui.activities;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class TempleOSRSOverview extends JPanel
{
	public JPanel playerOverview;

	public TempleOSRSOverviewLabel EXP;

	public TempleOSRSOverviewLabel EHP;

	public TempleOSRSOverviewLabel EHB;

	public TempleOSRSOverview()
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel playerInfoLabel = new JLabel("Overview");
		playerInfoLabel.setBorder(new EmptyBorder(5, 5, 0, 0));
		playerInfoLabel.setFont(FontManager.getRunescapeBoldFont());
		playerInfoLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		JPanel infoSelection = new JPanel();
		infoSelection.setLayout(new BoxLayout(infoSelection, BoxLayout.Y_AXIS));
		infoSelection.add(playerInfoLabel);

		layoutPanel.add(infoSelection);

		playerOverview = new JPanel();
		playerOverview.setBorder(new EmptyBorder(5, 5, 5, 5));
		playerOverview.setLayout(new GridLayout(3, 1, 0, 5));

		EHP = new TempleOSRSOverviewLabel("Ehp", "Efficient Hours Played");
		EHB = new TempleOSRSOverviewLabel("Ehb", "Efficient Hours Bossed");
		EXP = new TempleOSRSOverviewLabel("Xp", "Total Experience");

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
