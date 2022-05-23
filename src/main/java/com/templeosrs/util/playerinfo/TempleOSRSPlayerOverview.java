package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleOSRSError;

public class TempleOSRSPlayerOverview
{
	@SerializedName("data")
	public TempleOSRSPlayerData data;

	@SerializedName("error")
	public TempleOSRSError error;
}
