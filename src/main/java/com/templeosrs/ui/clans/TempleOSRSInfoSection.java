package com.templeosrs.ui.clans;

import static com.templeosrs.ui.TempleOSRSPanel.DEFAULT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleOSRSInfoSection extends JPanel
{
	JLabel value;

	TempleOSRSInfoSection(String label, Color color)
	{
		this.value = new JLabel(DEFAULT);
		value.setFont(FontManager.getRunescapeSmallFont());

		setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		setBorder(new EmptyBorder(0, 10, 0, 0));
		setLayout(new GridLayout(1, 2));
		setBackground(color);

		JLabel field = new JLabel(label, SwingConstants.LEFT);
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);

		add(field);
		add(value);
	}

	void reset()
	{
		value.setText(DEFAULT);
	}
}
