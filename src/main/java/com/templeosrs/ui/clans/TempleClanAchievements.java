package com.templeosrs.ui.clans;

import com.templeosrs.TempleOSRSPlugin;
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

	TempleClanAchievements(TempleOSRSPlugin plugin, List<TempleClanAchievement> clanActivityList)
	{
		if (clanActivityList == null)
		{
			return;
		}

		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		layoutPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		/* layout which holds all achievement-rows */
		JPanel clanActivity = new JPanel();
		clanActivity.setLayout(new GridLayout(0, 1));

		/* for each achievement in activity-list */
		for (int i = 0; i < clanActivityList.size(); i++)
		{
			TempleClanAchievement skill = clanActivityList.get(i);
			HiscoreSkillType type = skill.type.equals("Skill") ? HiscoreSkillType.SKILL : HiscoreSkillType.BOSS;

			/* create a new achievement-row and add to clan-activity layout */
			TempleClanAchievementRow row = new TempleClanAchievementRow(plugin, skill.username, skill.skill, type, skill.xp, COLORS[i % 2]);
			clanActivity.add(row);
		}

		/* add clan-activity list to main layout */
		layoutPanel.add(clanActivity, BorderLayout.SOUTH);

		/* create custom border */
		TitledBorder custom = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), "Recent Activity");
		custom.setTitleColor(ColorScheme.GRAND_EXCHANGE_LIMIT);
		custom.setTitleJustification(TitledBorder.CENTER);
		custom.setTitleFont(FontManager.getRunescapeSmallFont());

		/* if list is too large -> add scroll-pane and set preferred dimensions */
		if (clanActivityList.size() > 10)
		{
			setPreferredSize(new Dimension(PANEL_WIDTH, 275));

			/* create and add scroll-pane */
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
