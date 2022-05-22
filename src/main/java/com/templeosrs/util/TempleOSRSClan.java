package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.claninfo.TempleOSRSAchievementData;
import com.templeosrs.util.claninfo.TempleOSRSClanOverview;
import java.util.Objects;

public class TempleOSRSClan
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSClanOverview clanOverview;

	public TempleOSRSAchievementData clanAchievements;

	TempleOSRSClan(String clanOverviewJSON, String clanAchievementsJSON)
	{
		clanOverview = gson.fromJson(clanOverviewJSON, TempleOSRSClanOverview.class);

		clanAchievements = gson.fromJson(clanAchievementsJSON, TempleOSRSAchievementData.class);

		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}
}
