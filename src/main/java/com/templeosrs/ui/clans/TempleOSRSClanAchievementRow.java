package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

public class TempleOSRSClanAchievementRow extends JPanel
{
	TempleOSRSClanAchievementRow(String name, String date, String skill, HiscoreSkillType type, long xp, Color color)
	{
		setLayout(new BorderLayout());
		setBackground(color);

		JPanel row = new JPanel();
		row.setLayout(new GridLayout(1, 3));
		row.setOpaque(false);
		row.setBorder(new EmptyBorder(2, 0, 2, 0));
		row.setPreferredSize(new Dimension(0, 25));

		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(new BorderLayout());
		iconPanel.setOpaque(false);
		String iconPath;

		String formattedName = skill.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

		if (type.equals(HiscoreSkillType.SKILL))
		{
			iconPath = "skills/skill_icon_" + formattedName + "1.png";
		}
		else
		{
			iconPath = "bosses/game_icon_" + formattedName + ".png";
		}

		JLabel iconLabel = new JLabel();
		iconLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

		ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));

		JLabel usernameLabel = createNewJLabel(name);
		row.add(usernameLabel);

		String xpType = type.equals(HiscoreSkillType.SKILL) ? QuantityFormatter.quantityToStackSize(xp) + " XP" : QuantityFormatter.quantityToStackSize(xp) + " KC";
		JLabel xpLabel = createNewJLabel(xpType);
		xpLabel.setHorizontalAlignment(JLabel.CENTER);
		row.add(xpLabel);

		JLabel skillIcon = new JLabel();
		skillIcon.setBorder(new EmptyBorder(0, 0, 0, 5));
		skillIcon.setIcon(icon);
		skillIcon.setToolTipText(skill);
		skillIcon.setHorizontalAlignment(JLabel.RIGHT);
		row.add(skillIcon);

		add(row);
	}

	JLabel createNewJLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setToolTipText(text);
		label.setFont(FontManager.getRunescapeSmallFont());
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		return label;
	}
}
