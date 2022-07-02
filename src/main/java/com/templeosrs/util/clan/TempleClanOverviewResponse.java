package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TempleClanOverviewResponse
{
	@SerializedName("data")
	public TempleClanOverviewData data;

	@SerializedName("error")
	public TempleError error;
}
