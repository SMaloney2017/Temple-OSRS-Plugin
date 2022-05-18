package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.playerinfo.TempleOSRSPlayerSkills;
import java.util.Objects;

public class TempleOSRSPlayer
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSPlayerSkills playerSkillsData;

	public TempleOSRSPlayerSkills playerBossingData;

	public TempleOSRSPlayer(String playerSkillsOverviewJSON, String playerBossingOverviewJSON)
	{
		playerSkillsData = gson.fromJson(playerSkillsOverviewJSON, TempleOSRSPlayerSkills.class);
		playerBossingData = gson.fromJson(playerBossingOverviewJSON, TempleOSRSPlayerSkills.class);

		if (Objects.nonNull(playerSkillsData.error) || Objects.nonNull(playerBossingData.error))
		{
			error = true;
		}
	}
}
