package com.templeosrs.ui.competitions;

import com.templeosrs.util.compinfo.TempleOSRSCompParticipant;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.QuantityFormatter;

public class TempleOSRSCompetitionRank extends JPanel
{
	TempleOSRSCompetitionRank(TempleOSRSCompParticipant player, Color color)
	{
		setLayout(new GridLayout(0, 2));
		setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		setBackground(color);

		JLabel name = new JLabel(player.username);
		name.setBorder(new EmptyBorder(0, 5, 0, 0));
		name.setFont(FontManager.getRunescapeSmallFont());

		add(name);

		JLabel xp = new JLabel(QuantityFormatter.quantityToStackSize(player.xpGained.longValue()));
		xp.setFont(FontManager.getRunescapeSmallFont());

		add(xp);
	}
}
