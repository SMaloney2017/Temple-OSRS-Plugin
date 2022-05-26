package com.templeosrs.ui.clans;

import com.templeosrs.util.clan.TempleClanAchievement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class TempleClanAchievements extends JPanel
{
	private static final Color[] COLORS = {ColorScheme.DARK_GRAY_HOVER_COLOR, ColorScheme.DARKER_GRAY_COLOR};

	TempleClanAchievements(List<TempleClanAchievement> clanActivityList)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* if the clan activity list is not null ->
		 *  { create a panel to house achievement-rows,
		 * 	 for each item in list ->
		 * 	 { create achievement-row,
		 * 	   add row to activity-panel }
		 * 	},
		 * 	add activity-panel to layout,
		 * 	set preferred size/ scrollbar if applicable
		 */
		if (clanActivityList != null)
		{
			JPanel clanActivity = new JPanel();
			clanActivity.setLayout(new GridLayout(0, 1));

			for (int i = 0; i < clanActivityList.size(); i++)
			{
				TempleClanAchievement skill = clanActivityList.get(i);
				HiscoreSkillType type = skill.type.equals("Skill") ? HiscoreSkillType.SKILL : HiscoreSkillType.BOSS;
				TempleClanAchievementRow row = new TempleClanAchievementRow(skill.username, skill.skill, type, skill.xp, COLORS[i % 2]);
				clanActivity.add(row);
			}

			layoutPanel.add(clanActivity, BorderLayout.SOUTH);

			TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), "Recent Activity");
			custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
			custom.setTitleJustification(TitledBorder.CENTER);
			custom.setTitleFont(FontManager.getRunescapeSmallFont());

			if (clanActivityList.size() > 10)
			{
				setPreferredSize(new Dimension(PANEL_WIDTH, 275));

				final JScrollPane scroll = new JScrollPane(layoutPanel);
				scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				scroll.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				scroll.setBorder(custom);
				add(scroll);
			}
			else
			{
				layoutPanel.setBorder(custom);
				add(layoutPanel);
			}
		}
	}
}
