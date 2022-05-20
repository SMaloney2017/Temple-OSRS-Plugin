package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.claninfo.TempleOSRSClanOverview;
import java.util.Objects;

public class TempleOSRSClan
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSClanOverview clanOverview;


	TempleOSRSClan(String clanOverviewJSON)
	{
		clanOverview = gson.fromJson(clanOverviewJSON, TempleOSRSClanOverview.class);

		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}
}
