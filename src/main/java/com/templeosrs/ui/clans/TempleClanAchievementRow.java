package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

public class TempleClanAchievementRow extends JPanel
{
	TempleClanAchievementRow(TempleOSRSPlugin plugin, String name, String skill, HiscoreSkillType type, long xp, Color color)
	{
		setLayout(new BorderLayout());

		JPanel row = new JPanel();
		row.setLayout(new GridLayout(1, 3));
		row.setBorder(new EmptyBorder(2, 0, 2, 0));
		row.setPreferredSize(new Dimension(0, 25));
		row.setBackground(color);

		/* format name for file-path */
		String formattedName = skill.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

		/* determine icon-path */
		String iconPath;
		if (type.equals(HiscoreSkillType.SKILL))
		{
			iconPath = "skills/skill_icon_" + formattedName + ".png";
		}
		else
		{
			iconPath = "bosses/game_icon_" + formattedName + ".png";
		}

		ImageIcon icon;
		try
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));
		}
		catch (Exception e)
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "bosses/game_icon_null.png"));
		}

		/* add username to row */
		JLabel usernameLabel = createNewJLabel(name);
		row.add(usernameLabel);

		/* add achievement-value to row */
		String xpType = QuantityFormatter.quantityToStackSize(xp);
		JLabel xpLabel = createNewJLabel(xpType);

		xpLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
		xpLabel.setHorizontalAlignment(JLabel.CENTER);
		row.add(xpLabel);

		/* add achievement-icon to row */
		JLabel iconLabel = new JLabel();
		iconLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
		iconLabel.setIcon(icon);
		iconLabel.setToolTipText(skill);
		iconLabel.setHorizontalAlignment(JLabel.RIGHT);
		row.add(iconLabel);

		add(row);

		/* add on-click menu to username label */
		JPopupMenu menu = new JPopupMenu();

		JMenuItem lookupPlayer = new JMenuItem();
		lookupPlayer.setText("<html>Lookup <span style='color:#6ee16e'>" + name + "</span></html>");
		lookupPlayer.addActionListener(e -> plugin.fetchUser(name));
		menu.add(lookupPlayer);

		/* show lookup player menu option on click mouse-event */
		usernameLabel.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				menu.show(row, e.getX(), e.getY());
			}
		});
	}

	/* create a new achievement label of similar style */
	JLabel createNewJLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setToolTipText(text);
		label.setFont(FontManager.getRunescapeSmallFont());
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		return label;
	}
}
