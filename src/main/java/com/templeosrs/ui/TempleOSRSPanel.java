package com.templeosrs.ui;

import com.templeosrs.ui.activities.TempleOSRSSkillsPanel;
import com.templeosrs.ui.clans.TempleOSRSGroups;
import com.templeosrs.ui.competitions.TempleOSRSCompetitions;
import com.templeosrs.util.NameAutocompleter;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.runelite.api.Client;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TempleOSRSPanel extends PluginPanel
{
	public static final String DEFAULT = "--";

	public final TempleOSRSSkillsPanel skills;

	public final TempleOSRSGroups groups;

	public final TempleOSRSCompetitions competitions;

	public final MaterialTabGroup tabGroup;

	public final MaterialTab skillsTab;

	public final MaterialTab groupsTab;

	public final MaterialTab competitionsTab;

	@Inject
	public TempleOSRSPanel(Client client, NameAutocompleter nameAutocompleter)
	{
		skills = new TempleOSRSSkillsPanel(client, nameAutocompleter);
		groups = new TempleOSRSGroups();
		competitions = new TempleOSRSCompetitions();

		getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel display = new JPanel();
		tabGroup = new MaterialTabGroup(display);

		skillsTab = new MaterialTab("Ranks", tabGroup, skills);
		groupsTab = new MaterialTab("Clans", tabGroup, groups);
		competitionsTab = new MaterialTab("Competitions", tabGroup, competitions);

		tabGroup.addTab(skillsTab);
		tabGroup.addTab(groupsTab);
		tabGroup.addTab(competitionsTab);

		tabGroup.select(skillsTab);

		add(tabGroup);
		add(display);
	}
}
