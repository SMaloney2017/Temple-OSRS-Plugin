package com.templeosrs.ui.clans;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TempleClanCurrentTop extends PluginPanel
{
	TempleClanCurrentTop(TempleClanCurrentTopList Ehp, TempleClanCurrentTopList Ehb)
	{
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setBorder(new EmptyBorder(0, 0, 0, 0));

		/* tab group to switch between EHP and EHB */
		JPanel display = new JPanel();
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);

		MaterialTab leadersTab = new MaterialTab("Hours Played", tabGroup, Ehp);
		MaterialTab membersTab = new MaterialTab("Hours Bossed", tabGroup, Ehb);

		tabGroup.addTab(leadersTab);
		tabGroup.addTab(membersTab);
		tabGroup.select(leadersTab);

		add(tabGroup);
		add(display);
	}
}
