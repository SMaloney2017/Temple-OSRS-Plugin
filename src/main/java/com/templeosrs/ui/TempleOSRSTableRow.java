package com.templeosrs.ui;

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
import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;
import net.runelite.client.util.ImageUtil;

public class TempleOSRSTableRow extends JPanel
{
	private static final String DEFAULT = "--";

	final String name;

	private final TempleOSRSActivityLabel skillGain;

	private final TempleOSRSActivityLabel rankGain;

	private final TempleOSRSActivityLabel ehpGain;

	long total;

	long rank;

	double ehp;

	TempleOSRSTableRow(String skillName, String tooltip, Color color, HiscoreSkillType type)
	{
		this.name = skillName;

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

		if (type.equals(HiscoreSkillType.SKILL))
		{
			iconPath = "skills/skill_icon_" + skillName + "1.png";
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

		ImageIcon icon = new ImageIcon(ImageUtil.loadImageResource(TempleOSRSPlugin.class, iconPath));

		iconLabel.setIcon(icon);
		iconLabel.setToolTipText(tooltip);

		iconPanel.add(iconLabel);
		row.add(iconPanel, BorderLayout.WEST);

		skillGain = new TempleOSRSActivityLabel();
		rankGain = new TempleOSRSActivityLabel();
		ehpGain = new TempleOSRSActivityLabel();

		row.add(skillGain);
		row.add(rankGain);
		row.add(ehpGain);
		add(row);
	}

	void update(long gain, long rank, double ehp)
	{
		this.total = gain;
		this.rank = rank;
		this.ehp = ehp;

		skillGain.update(gain);
		rankGain.update(rank);
		ehpGain.update(ehp);
	}

	void reset()
	{
		skillGain.setText(DEFAULT);
		skillGain.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

		rankGain.setText(DEFAULT);
		rankGain.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

		ehpGain.setText(DEFAULT);
		ehpGain.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
	}
}
