package com.templeosrs.ui.ranks;

import com.templeosrs.TempleOSRSConfig;
import com.templeosrs.util.PlayerRanges;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleRanksDuration extends JPanel
{
	private static final String[] OPTIONS = new String[PlayerRanges.values().length];
	public static JComboBox<String> jComboBox;

	static
	{
		for (int i = 0; i < PlayerRanges.values().length; i++)
		{
			OPTIONS[i] = PlayerRanges.values()[i].getName();
		}
	}

	public TempleRanksDuration(TempleOSRSConfig config, TempleRanks panel)
	{
		setLayout(new BorderLayout());
		setOpaque(false);

		setMinimumSize(new Dimension(PANEL_WIDTH, 20));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		jComboBox = new JComboBox<>(OPTIONS);
		jComboBox.addActionListener(e -> panel.fetchUser());
		jComboBox.setSelectedItem(config.getDefaultRange().getName());

		add(jComboBox);
	}
}
