package com.templeosrs.ui.clans;

import com.templeosrs.util.clan.TempleClanAchievement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleClanAchievements extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARK_GRAY_HOVER_COLOR};

	TempleClanAchievements(List<TempleClanAchievement> clanActivityList)
	{
		setLayout(new GridLayout(0, 1));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel(new BorderLayout());

		JLabel achievementHeaderLabel = new JLabel("Recent Activity");
		achievementHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		achievementHeaderLabel.setBorder(new EmptyBorder(5, 5, 5, 0));
		achievementHeaderLabel.setFont(FontManager.getRunescapeBoldFont());
		achievementHeaderLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);

		layoutPanel.add(achievementHeaderLabel);

		if (clanActivityList != null)
		{
			JPanel clanActivity = new JPanel();
			clanActivity.setLayout(new GridLayout(0, 1, 0, 2));

			for (int i = 0; i < clanActivityList.size(); i++)
			{
				TempleClanAchievement skill = clanActivityList.get(i);
				HiscoreSkillType type = skill.type.equals("Skill") ? HiscoreSkillType.SKILL : HiscoreSkillType.BOSS;
				TempleClanAchievementRow row = new TempleClanAchievementRow(skill.username, skill.date, skill.skill, type, skill.xp, COLORS[i % 2]);
				clanActivity.add(row);
			}

			layoutPanel.add(clanActivity, BorderLayout.SOUTH);

			if (clanActivityList.size() > 10)
			{
				setPreferredSize(new Dimension(PANEL_WIDTH, 250));
			}

			final JScrollPane scroll = new JScrollPane(layoutPanel);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBorder(new LineBorder(ColorScheme.SCROLL_TRACK_COLOR, 1));

			add(scroll);
		}
	}
}
