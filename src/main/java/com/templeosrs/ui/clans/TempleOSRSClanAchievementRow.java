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
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.ImageUtil;

public class TempleOSRSClanAchievementRow extends JPanel
{
	TempleOSRSClanAchievementRow(String username, String date, String skill, HiscoreSkillType type, long xp, Color color)
	{
		setLayout(new BorderLayout());
		setBackground(color);

		JPanel row = new JPanel();
		row.setLayout(new GridLayout());
		row.setOpaque(false);
		row.setBorder(new EmptyBorder(2, 0, 2, 0));
		row.setPreferredSize(new Dimension(PANEL_WIDTH, 25));

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
	}
}
