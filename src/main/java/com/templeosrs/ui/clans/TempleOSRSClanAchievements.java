package com.templeosrs.ui.clans;

import com.templeosrs.util.claninfo.TempleOSRSClanAchievementSkill;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class TempleOSRSClanAchievements extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARK_GRAY_HOVER_COLOR};

	TempleOSRSClanAchievements(List<TempleOSRSClanAchievementSkill> clanActivityList)
	{
		setLayout(new GridLayout(0, 1));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

		JLabel achievementHeaderLabel = new JLabel("Recent Activity");
		achievementHeaderLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
		achievementHeaderLabel.setFont(FontManager.getRunescapeBoldFont());
		achievementHeaderLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		JPanel infoSelection = new JPanel();
		infoSelection.setLayout(new BoxLayout(infoSelection, BoxLayout.Y_AXIS));
		infoSelection.add(achievementHeaderLabel);

		layoutPanel.add(infoSelection);

		JPanel clanActivity = new JPanel();
		clanActivity.setLayout(new GridLayout(0, 1, 0, 2));

		for(int i = 0; i < clanActivityList.size(); i++)
		{
			TempleOSRSClanAchievementSkill skill = clanActivityList.get(i);
			System.out.println(i + skill.username);
			HiscoreSkillType type = skill.type.equals("Skill") ? HiscoreSkillType.SKILL : HiscoreSkillType.BOSS;
			TempleOSRSClanAchievementRow row = new TempleOSRSClanAchievementRow(skill.username, skill.date, skill.skill, type, skill.xp, COLORS[i % 2]);
			add(row);
		}
	}
}
