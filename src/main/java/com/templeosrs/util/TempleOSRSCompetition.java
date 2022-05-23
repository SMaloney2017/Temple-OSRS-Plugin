package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.compinfo.TempleOSRSComp;
import java.util.Objects;

public class TempleOSRSCompetition
{

	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSComp clanOverview;

	TempleOSRSCompetition(String competitionJSON)
	{
		clanOverview = gson.fromJson(competitionJSON, TempleOSRSComp.class);


		if (Objects.nonNull(clanOverview.error))
		{
			error = true;
		}
	}

}
