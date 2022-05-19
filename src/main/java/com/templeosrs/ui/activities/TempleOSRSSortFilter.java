package com.templeosrs.ui.activities;

import com.templeosrs.TempleOSRSPlugin;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

public class TempleOSRSSortFilter extends JPanel
{
	private final JLabel icon;

	JLabel label;

	boolean increasing;

	TempleOSRSSortFilter(String text)
	{
		icon = new JLabel();

		label = new JLabel(text);
		label.setFont(FontManager.getRunescapeSmallFont());

		setLayout(new GridLayout(0, 2));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				increasing = !increasing;
				icon.setIcon(new ImageIcon(increasing ? ImageUtil.loadImageResource(TempleOSRSPlugin.class, "sorting/up.png") : ImageUtil.loadImageResource(TempleOSRSPlugin.class, "sorting/down.png")));
			}
		});

		add(label);
		add(icon);
	}

	void reset()
	{
		icon.setIcon(null);
	}
}