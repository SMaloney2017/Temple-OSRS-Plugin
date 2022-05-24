package com.templeosrs.util.compinfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TempleCompetitionOverview
{
	@SerializedName("data")
	public TempleCompetitionData data;

	@SerializedName("error")
	public TempleError error;
}
