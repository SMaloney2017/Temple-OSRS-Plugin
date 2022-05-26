package com.templeosrs.ui.competitions;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.comp.TempleCompetitionParticipant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.QuantityFormatter;

public class TempleCompetitionRow extends JPanel
{
	TempleCompetitionRow(TempleOSRSPlugin plugin, TempleCompetitionParticipant player, int i, Color color)
	{
		setLayout(new BorderLayout());

		JPanel row = new JPanel();
		row.setLayout(new GridLayout());
		row.setBackground(color);
		row.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		/* add start-point and current-end-point as tooltip-text */
		row.setToolTipText("<html>Start: <span style='color:#32a0fa'>" + QuantityFormatter.quantityToStackSize(player.startXp.longValue()) + "</span><br>End: <span style='color:#32a0fa'>" + QuantityFormatter.quantityToStackSize(player.endXp.longValue()) + "</span></html>");

		/* add player and ranking */
		JLabel name = new JLabel(i + ". " + player.username);
		name.setBorder(new EmptyBorder(0, 5, 0, 0));
		name.setFont(FontManager.getRunescapeSmallFont());
		row.add(name);

		/* add formatted total */
		JLabel xp = new JLabel(QuantityFormatter.quantityToStackSize(player.xpGained.longValue()));
		xp.setBorder(new EmptyBorder(0, 5, 0, 0));
		xp.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
		xp.setFont(FontManager.getRunescapeSmallFont());
		row.add(xp);

		/* add on-click menu to row */
		JPopupMenu menu = new JPopupMenu();

		JMenuItem lookupPlayer = new JMenuItem();
		lookupPlayer.setText("<html>Lookup <span style='color:#6ee16e'>" + player.username + "</span></html>");
		lookupPlayer.addActionListener(e -> plugin.fetchUser(player.username));
		menu.add(lookupPlayer);

		/* show lookup player menu option on click mouse-event */
		row.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				menu.show(row, e.getX(), e.getY());
			}
		});

		add(row);
	}
}
