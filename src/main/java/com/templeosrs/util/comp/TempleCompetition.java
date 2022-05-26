package com.templeosrs.util.comp;

import com.google.gson.Gson;
import java.util.Objects;

public class TempleCompetition
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleCompetitionResponse compOverview;

	public TempleCompetition(String competitionJSON)
	{
		compOverview = gson.fromJson(competitionJSON, TempleCompetitionResponse.class);

		if (Objects.nonNull(compOverview.error))
		{
			error = true;
		}
	}

}
