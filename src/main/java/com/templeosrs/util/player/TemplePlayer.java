package com.templeosrs.util.player;

import com.google.gson.Gson;
import java.util.Objects;

public class TemplePlayer
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TemplePlayerOverview playerSkillsOverview;

	public TemplePlayerOverview playerBossesOverview;

	public TemplePlayer(String playerSkillsOverviewJSON, String playerBossingOverviewJSON)
	{
		playerSkillsOverview = gson.fromJson(playerSkillsOverviewJSON, TemplePlayerOverview.class);
		playerBossesOverview = gson.fromJson(playerBossingOverviewJSON, TemplePlayerOverview.class);

		if (Objects.nonNull(playerSkillsOverview.error) || Objects.nonNull(playerBossesOverview.error))
		{
			error = true;
		}
	}
}
