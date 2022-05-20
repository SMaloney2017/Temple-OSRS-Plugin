package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSPlayerOverview
{
	@SerializedName("data")
	public TempleOSRSPlayerData data;

	@SerializedName("error")
	public TempleOSRSPlayerError error;
}
