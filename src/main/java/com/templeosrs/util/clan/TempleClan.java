package com.templeosrs.util.clan;

import com.google.gson.Gson;
import java.util.Objects;

public class TempleClan
{
	public boolean error = false;

	public TempleClanOverviewResponse clanOverview;

	public TempleClanAchievementResponse clanAchievements;

	public TempleClanCurrentTopResponse clanCurrentTopEhp;

	public TempleClanCurrentTopResponse clanCurrentTopEhb;

	public TempleClan(String clanOverviewJSON, String clanAchievementsJSON, String clanCurrentTopEhpJSON, String clanCurrentTopEhbJSON, Gson gson)
	{
		clanOverview = gson.fromJson(clanOverviewJSON, TempleClanOverviewResponse.class);

		clanAchievements = gson.fromJson(clanAchievementsJSON, TempleClanAchievementResponse.class);

		clanCurrentTopEhp = gson.fromJson(clanCurrentTopEhpJSON, TempleClanCurrentTopResponse.class);

		clanCurrentTopEhb = gson.fromJson(clanCurrentTopEhbJSON, TempleClanCurrentTopResponse.class);

		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}
}
