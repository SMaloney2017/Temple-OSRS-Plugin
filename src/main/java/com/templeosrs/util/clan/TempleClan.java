package com.templeosrs.util.clan;

import com.google.gson.Gson;
import java.util.Objects;

public class TempleClan
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleClanResponse clanOverview;

	public TempleClanAchievementData clanAchievements;

	public TempleClan(String clanOverviewJSON, String clanAchievementsJSON)
	{
		clanOverview = gson.fromJson(clanOverviewJSON, TempleClanResponse.class);

		clanAchievements = gson.fromJson(clanAchievementsJSON, TempleClanAchievementData.class);

		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}
}
