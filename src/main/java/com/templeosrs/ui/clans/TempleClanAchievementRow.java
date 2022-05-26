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
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

public class TempleClanAchievementRow extends JPanel
{
	TempleClanAchievementRow(String name, String skill, HiscoreSkillType type, long xp, Color color)
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

		JLabel iconLabel = new JLabel();
		iconLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

		ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));

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
		JLabel skillIcon = new JLabel();
		skillIcon.setBorder(new EmptyBorder(0, 0, 0, 5));
		skillIcon.setIcon(icon);
		skillIcon.setToolTipText(skill);
		skillIcon.setHorizontalAlignment(JLabel.RIGHT);
		row.add(skillIcon);

		add(row);
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
