package com.templeosrs.ui.activities;

import javax.swing.JLabel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.QuantityFormatter;

public class TempleOSRSActivityLabel extends JLabel
{
	private static final String DEFAULT = "--";

	TempleOSRSActivityLabel()
	{
		setText(DEFAULT);
		setFont(FontManager.getRunescapeSmallFont());
		setForeground(ColorScheme.LIGHT_GRAY_COLOR);
	}

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
			setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		}
		else
		{
			setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		}
	}

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
			setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		}
		else
		{
			setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		}
	}
}
