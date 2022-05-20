package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.playerinfo.TempleOSRSPlayerOverview;
import java.util.Objects;

public class TempleOSRSPlayer
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSPlayerOverview playerSkillsOverview;

	public TempleOSRSPlayerOverview playerBossesOverview;

	public TempleOSRSPlayer(String playerSkillsOverviewJSON, String playerBossingOverviewJSON)
	{
		playerSkillsOverview = gson.fromJson(playerSkillsOverviewJSON, TempleOSRSPlayerOverview.class);
		playerBossesOverview = gson.fromJson(playerBossingOverviewJSON, TempleOSRSPlayerOverview.class);

		if (Objects.nonNull(playerSkillsOverview.error) || Objects.nonNull(playerBossesOverview.error))
		{
			error = true;
		}
	}
}
