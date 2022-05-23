package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.compinfo.TempleOSRSCompOverview;
import java.util.Objects;

public class TempleOSRSCompetition
{

	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSCompOverview clanOverview;

	TempleOSRSCompetition(String competitionJSON)
	{
		clanOverview = gson.fromJson(competitionJSON, TempleOSRSCompOverview.class);


		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}

}
