package com.templeosrs.ui.ranks;

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

public class TempleRanksOverviewSection extends JPanel
{
	JLabel total;

	JLabel rank;

	private static final String LOADING = "...";

	TempleRanksOverviewSection(String label, String tooltip, Color color)
	{
		this.total = createNewJLabel(label + " Total");
		this.rank = createNewJLabel(label + " Rank");

		setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		setBorder(new EmptyBorder(0, 10, 0, 0));
		setLayout(new GridLayout(1, 3));
		setBackground(color);

		/* add labels to layout */
		JLabel field = new JLabel(label.toUpperCase(), SwingConstants.LEFT);
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
		field.setToolTipText(tooltip);

		add(field);
		add(total);
		add(rank);
	}

	/* update overview-section (rank, total) pair */
	public void update(long x, long x1)
	{
		setRank(x);
		setTotal(x1);
	}

	void setRank(long x)
	{
		rank.setText(x != 0 ? QuantityFormatter.quantityToStackSize(x) : DEFAULT);
	}

	void setTotal(long x1)
	{
		total.setText(x1 != 0 ? QuantityFormatter.quantityToStackSize(x1) : DEFAULT);
	}

	void setRankLoading()
	{
		rank.setText(LOADING);
	}

	/* rank could not be determined; explain why in its tooltip */
	void setRankUnavailable(String reason)
	{
		rank.setText(DEFAULT);
		rank.setToolTipText(reason);
	}

	/* reset overview-section (rank, total) pair */
	void reset()
	{
		total.setText(DEFAULT);
		rank.setText(DEFAULT);
	}

	/* indicate the (rank, total) pair is being fetched */
	void loading()
	{
		total.setText(LOADING);
		rank.setText(LOADING);
	}

	void setTooltips(String totalTooltip, String rankTooltip)
	{
		total.setToolTipText(totalTooltip);
		rank.setToolTipText(rankTooltip);
	}

	/* creates an overview-section label of similar style */
	private JLabel createNewJLabel(String tooltip)
	{
		JLabel label = new JLabel(DEFAULT);

		label.setFont(FontManager.getRunescapeSmallFont());
		label.setToolTipText(tooltip);

		return label;
	}
}
