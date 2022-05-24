package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.compinfo.TempleOSRSCompOverview;
import java.util.Objects;

public class TempleOSRSCompetition
{

	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSCompOverview compOverview;

	TempleOSRSCompetition(String competitionJSON)
	{
		try
		{
			compOverview = gson.fromJson(competitionJSON, TempleOSRSCompOverview.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (Objects.nonNull(compOverview.error))
		{
			error = true;
		}
	}

}
