package com.templeosrs.ui.ranks;

import static com.templeosrs.ui.TempleOSRSPanel.DEFAULT;
import javax.swing.JLabel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.QuantityFormatter;

public class TempleActivityLabel extends JLabel
{
	TempleActivityLabel()
	{
		setText(DEFAULT);
		setFont(FontManager.getRunescapeSmallFont());
		setForeground(ColorScheme.LIGHT_GRAY_COLOR);
	}

	/* update activity-row label (xp, level, rank) */
	void update(long value)
	{
		setText(QuantityFormatter.quantityToStackSize(value));
		if (value == 0)
		{
			setForeground(ColorScheme.LIGHT_GRAY_COLOR);
			setText(DEFAULT);
		}
		else if (value > 0)
		{
			setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
		}
		else
		{
			setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		}
	}

	/* update (double) activity-row label (ehp, ehb) */
	void update(double value)
	{
		setText(String.format("%.2f", value));
		if (value == 0)
		{
			setForeground(ColorScheme.LIGHT_GRAY_COLOR);
			setText(DEFAULT);
		}
		else if (value > 0)
		{
			setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
		}
		else
		{
			setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		}
	}

	/* reset activity-row label to default */
	void reset()
	{
		setText(DEFAULT);
		setForeground(ColorScheme.LIGHT_GRAY_COLOR);
	}
}
