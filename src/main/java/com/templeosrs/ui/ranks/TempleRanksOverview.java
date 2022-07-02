package com.templeosrs.ui.ranks;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class TempleRanksOverview extends JPanel
{
	public final TempleRanksOverviewSection EXP;

	public final TempleRanksOverviewSection EHP;

	public final TempleRanksOverviewSection EHB;

	public TempleRanksOverview()
	{
		setLayout(new GridLayout(0, 1));
		setBorder(new EmptyBorder(5, -2, 5, -2));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* create custom border for player-overview */
		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), "Player Overview");
		custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
		custom.setTitleJustification(TitledBorder.CENTER);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		layoutPanel.setBorder(custom);

		JPanel playerOverview = new JPanel();
		playerOverview.setBorder(new EmptyBorder(3, 3, 3, 3));
		playerOverview.setLayout(new GridLayout(3, 1));
		playerOverview.setOpaque(false);

		/* create overview sections and add to player-overview panel */
		EHP = new TempleRanksOverviewSection("Ehp", "Efficient Hours Played", ColorScheme.DARK_GRAY_HOVER_COLOR);
		EHB = new TempleRanksOverviewSection("Ehb", "Efficient Hours Bossed", ColorScheme.DARKER_GRAY_COLOR);
		EXP = new TempleRanksOverviewSection("Xp", "Total Experience", ColorScheme.DARK_GRAY_HOVER_COLOR);

		playerOverview.add(EHP);
		playerOverview.add(EHB);
		playerOverview.add(EXP);

		layoutPanel.add(playerOverview, BorderLayout.SOUTH);
		add(layoutPanel);
	}

	/* reset player-overview sections */
	public void reset()
	{
		EHP.reset();
		EHB.reset();
		EXP.reset();
	}
}
