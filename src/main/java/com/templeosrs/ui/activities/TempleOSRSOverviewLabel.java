package com.templeosrs.ui.activities;

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
import net.runelite.client.util.QuantityFormatter;

public class TempleOSRSOverviewLabel extends JPanel
{
	JLabel total;

	JLabel rank;

	TempleOSRSOverviewLabel(String label, String tooltip)
	{
		this.total = createNewJLabel(label + " Total");
		this.rank = createNewJLabel(label + " Rank");

		setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		setBorder(new EmptyBorder(0, 10, 0, 0));
		setBackground(ColorScheme.SCROLL_TRACK_COLOR);
		setLayout(new GridLayout(1, 3));

		JLabel field = new JLabel(label.toUpperCase(), SwingConstants.LEFT);
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		field.setToolTipText(tooltip);

		add(field);
		add(total);
		add(rank);
	}

	public void update(long x, long x1)
	{
		rank.setText(x != 0 ? QuantityFormatter.quantityToStackSize(x) : DEFAULT);
		total.setText(x1 != 0 ? QuantityFormatter.quantityToStackSize(x1) : DEFAULT);
	}

	void reset()
	{
		total.setText(DEFAULT);
		rank.setText(DEFAULT);
	}

	private JLabel createNewJLabel(String tooltip)
	{
		JLabel label = new JLabel(DEFAULT);

		label.setFont(FontManager.getRunescapeSmallFont());
		label.setForeground(Color.WHITE);
		label.setToolTipText(tooltip);

		return label;
	}
}
