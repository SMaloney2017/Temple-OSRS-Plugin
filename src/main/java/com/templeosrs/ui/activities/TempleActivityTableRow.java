/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * Copyright (c) 2019, Bram91 <https://github.com/bram91>
 * Copyright (c) 2020, dekvall
 * Copyright (c) 2021, Rorro
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.templeosrs.ui.activities;

import com.templeosrs.TempleOSRSPlugin;
import static com.templeosrs.ui.TemplePanel.DEFAULT;
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
		row.setOpaque(false);
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

		skillGain = new TempleActivityLabel();
		rankGain = new TempleActivityLabel();
		ehpGain = new TempleActivityLabel();

		row.add(skillGain);
		row.add(rankGain);
		row.add(ehpGain);
		add(row);
	}

	void update(long gain, long level, long rank, double ehp)
	{
		this.total = gain;
		this.rank = rank;
		this.ehp = ehp;

		skillGain.update(gain);
		skillGain.setToolTipText(type.equals(HiscoreSkillType.SKILL) ? level + " Levels" : "");
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
