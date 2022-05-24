package com.templeosrs.ui.competitions;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSRankingsHeader extends JPanel
{
	TempleOSRSRankingsHeader()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
		setPreferredSize(new Dimension(PANEL_WIDTH, 20));
		setLayout(new GridLayout(0, 2));

		JLabel label = new JLabel("Name");
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		label.setFont(FontManager.getRunescapeSmallFont());
		add(label);

		JLabel gain = new JLabel("Total");
		gain.setBorder(new EmptyBorder(0, 5, 0, 0));
		gain.setFont(FontManager.getRunescapeSmallFont());
		add(gain);
	}
}
