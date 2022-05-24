package com.templeosrs.ui.competitions;

import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.ui.activities.TempleActivity.BOSSES;
import static com.templeosrs.ui.activities.TempleActivity.SKILLS;
import com.templeosrs.util.TempleHiscoreSkill;
import com.templeosrs.util.compinfo.TempleCompetitionInfo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

public class TempleCompetitionOverview extends JPanel
{

	TempleCompetitionOverview(TempleCompetitionInfo info, int memberCount)
	{
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		layoutPanel.setOpaque(false);

		JLabel compName = new JLabel(info.name);
		compName.setFont(FontManager.getRunescapeBoldFont());
		compName.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		compName.setAlignmentX(Component.CENTER_ALIGNMENT);
		compName.setToolTipText(info.name);
		layoutPanel.add(compName);

		JPanel fieldLayout = new JPanel();
		fieldLayout.setLayout(new FlowLayout());
		fieldLayout.setOpaque(false);

		JLabel field = new JLabel("Participants:");
		field.setFont(FontManager.getRunescapeSmallFont());
		field.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldLayout.add(field);

		JLabel count = new JLabel(String.valueOf(memberCount));
		count.setFont(FontManager.getRunescapeSmallFont());
		fieldLayout.add(count);

		JPanel statusLayout = new JPanel();
		statusLayout.setLayout(new FlowLayout());
		statusLayout.setOpaque(false);

		JLabel statusLabel = new JLabel("Status:");
		statusLabel.setFont(FontManager.getRunescapeSmallFont());
		statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		statusLayout.add(statusLabel);

		JLabel status = new JLabel(info.statusText);
		status.setFont(FontManager.getRunescapeSmallFont());
		if (info.statusText.equals("Finished"))
		{
			status.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		}
		else if (info.statusText.equals("In progress"))
		{
			status.setForeground(ColorScheme.PROGRESS_INPROGRESS_COLOR);
		}
		else
		{
			status.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
		}
		statusLayout.add(status);

		layoutPanel.add(statusLayout);

		layoutPanel.add(fieldLayout);

		String iconPath;

		switch (info.skill)
		{
			case "EHB":
			case "EHP + EHB":
				iconPath = "bosses/game_icon_ehb.png";
				break;
			case "EHP":
				iconPath = "skills/skill_icon_ehp1.png";
				break;
			case "F2P EHP":
				iconPath = "skills/skill_icon_f2p.png";
				break;
			case "Lvl-3 EHP":
				iconPath = "skills/skill_icon_lvl3.png";
				break;
			case "Iron EHP":
				iconPath = "skills/skill_icon_ironehp1.png";
				break;
			default:
				iconPath = "overall.png";
				break;
		}


		for (TempleHiscoreSkill skill : SKILLS)
		{
			String skillName = skill.getName();
			String formattedName = skillName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			if (skillName.equals(info.skill))
			{
				iconPath = "skills/skill_icon_" + formattedName + "1.png";
				break;
			}
		}

		for (TempleHiscoreSkill skill : BOSSES)
		{
			String skillName = skill.getName();
			String formattedName = skillName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

			if (skillName.equals(info.skill))
			{
				iconPath = "bosses/game_icon_" + formattedName + ".png";
				break;
			}
		}

		JLabel iconLabel = new JLabel();

		ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));
		iconLabel.setIcon(icon);
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconLabel.setToolTipText(info.skill);

		layoutPanel.add(iconLabel);

		add(layoutPanel);
	}
}
