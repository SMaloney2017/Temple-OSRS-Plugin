package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSClanOverview
{
	@SerializedName("data")
	public TempleOSRSClanData data;

	@SerializedName("error")
	public TempleOSRSClanError error;
}
