package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.claninfo.TempleClanAchievementData;
import com.templeosrs.util.claninfo.TempleClanOverview;
import java.util.Objects;

public class TempleClan
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleClanOverview clanOverview;

	public TempleClanAchievementData clanAchievements;

	TempleClan(String clanOverviewJSON, String clanAchievementsJSON)
	{
		clanOverview = gson.fromJson(clanOverviewJSON, TempleClanOverview.class);

		clanAchievements = gson.fromJson(clanAchievementsJSON, TempleClanAchievementData.class);

		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}
}