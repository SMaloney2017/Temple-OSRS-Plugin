package com.templeosrs.util.comp;

import com.google.gson.Gson;
import java.util.Objects;

public class TempleCompetition
{

	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleCompetitionOverview compOverview;

	public TempleCompetition(String competitionJSON)
	{
		try
		{
			compOverview = gson.fromJson(competitionJSON, TempleCompetitionOverview.class);
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
