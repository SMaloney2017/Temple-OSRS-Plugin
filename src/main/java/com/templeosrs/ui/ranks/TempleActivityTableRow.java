package com.templeosrs.ui.ranks;

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

public class TempleActivityTableRow extends JPanel
{
	final String name;

	private final TempleActivityLabel skillGain;

	private final TempleActivityLabel rankGain;

	private final TempleActivityLabel ehpGain;

	long total;

	long rank;

	double ehp;

	HiscoreSkillType type;

	TempleActivityTableRow(String skillName, String tooltip, Color color, HiscoreSkillType type)
	{
		this.name = skillName;
		this.type = type;

		setLayout(new BorderLayout());
		setBackground(color);

		JPanel row = new JPanel();
		row.setLayout(new GridLayout());
		row.setPreferredSize(new Dimension(PANEL_WIDTH, 25));
		row.setOpaque(false);

		JPanel iconPanel = new JPanel();
		iconPanel.setLayout(new BorderLayout());
		iconPanel.setOpaque(false);

		/* determine icon-path by skill type and formatted-name */
		String iconPath;
		if (type.equals(HiscoreSkillType.SKILL))
		{
			iconPath = "skills/skill_icon_" + skillName + ".png";
		}
		else if (type.equals(HiscoreSkillType.BOSS))
		{
			iconPath = "bosses/game_icon_" + skillName + ".png";
		}
		else
		{
			iconPath = "overall.png";
		}

		JLabel iconLabel = new JLabel();
		iconLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

		/* load icon resource from path */
		ImageIcon icon;
		try
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));
		}
		catch (Exception e)
		{
			icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, "skills/skill_icon_null.png"));
		}

		iconLabel.setIcon(icon);
		iconLabel.setToolTipText(tooltip);

		/* add icon to row */
		iconPanel.add(iconLabel);
		row.add(iconPanel, BorderLayout.WEST);

		/* create and add labels to row */
		skillGain = new TempleActivityLabel();
		rankGain = new TempleActivityLabel();
		ehpGain = new TempleActivityLabel();

		row.add(skillGain);
		row.add(rankGain);
		row.add(ehpGain);
		add(row);
	}

	/* update activity-row values */
	void update(long gain, long level, long rank, double ehp)
	{
		this.total = gain;
		this.rank = rank;
		this.ehp = ehp;

		/* add levels gained as tooltip-text if SKILL */
		skillGain.update(gain);
		skillGain.setToolTipText(type.equals(HiscoreSkillType.SKILL) ? "<html>Levels Gained: <span style='color:#32a0fa'>" + level + "</span></html>" : "");

		rankGain.update(rank);
		ehpGain.update(ehp);
	}

	/* reset activity-row values to default */
	void reset()
	{
		skillGain.reset();
		rankGain.reset();
		ehpGain.reset();
	}
}
