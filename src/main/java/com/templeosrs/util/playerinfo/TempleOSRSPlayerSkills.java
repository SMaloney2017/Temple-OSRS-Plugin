package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSPlayerSkills
{
	@SerializedName("data")
	public TempleOSRSData data;

	@SerializedName("error")
	public TempleOSRSPlayerError error;
}
