package com.templeosrs.ui.competitions;

import com.templeosrs.TempleOSRSPlugin;
import com.templeosrs.util.TempleHiscoreSkill;
import com.templeosrs.util.comp.TempleCompetitionInfo;
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
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

public class TempleCompetitionOverview extends JPanel
{

	TempleCompetitionOverview(TempleCompetitionInfo info, int memberCount)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, ColorScheme.DARK_GRAY_COLOR, ColorScheme.SCROLL_TRACK_COLOR), new EmptyBorder(5, 5, 5, 5)));
		layoutPanel.setOpaque(false);

		/* add competition name to layout */
		String name = info.name;
		JLabel compName = new JLabel(name);
		compName.setFont(FontManager.getRunescapeBoldFont());
		compName.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		compName.setAlignmentX(Component.CENTER_ALIGNMENT);
		compName.setToolTipText(name);
		layoutPanel.add(compName);

		/* add participants-count to layout */
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

		/* add status to layout */
		JLabel statusLabel = new JLabel("Status:");
		statusLabel.setFont(FontManager.getRunescapeSmallFont());
		statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		statusLayout.add(statusLabel);

		String statusText = info.statusText;
		JLabel status = new JLabel(statusText);
		status.setFont(FontManager.getRunescapeSmallFont());

		/* add set status-text color */
		if (statusText.equals("Finished"))
		{
			status.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
		}
		else if (statusText.equals("In progress"))
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

		/* get HiscoreSkill by index */
		TempleHiscoreSkill skill = TempleHiscoreSkill.values()[info.skillIndex];

		String skillName = skill.getName();
		String formattedName = skillName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

		/* determine skill's icon-path */
		String iconPath;
		if (skill.getType().equals(HiscoreSkillType.SKILL))
		{
			iconPath = "skills/skill_icon_" + formattedName + ".png";
		}
		else
		{
			iconPath = "bosses/game_icon_" + formattedName + ".png";
		}

		JLabel iconLabel = new JLabel();

		/* add icon to layout */
		ImageIcon icon;
		try
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));
		}
		catch (Exception e)
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "bosses/game_icon_null.png"));
		}
		iconLabel.setIcon(icon);
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		iconLabel.setToolTipText(info.skill);

		layoutPanel.add(iconLabel);

		add(layoutPanel);
	}
}
