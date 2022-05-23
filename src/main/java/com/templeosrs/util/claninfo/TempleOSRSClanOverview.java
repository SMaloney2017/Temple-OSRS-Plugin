package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleOSRSError;

public class TempleOSRSClanOverview
{
	@SerializedName("data")
	public TempleOSRSClanData data;

	@SerializedName("error")
	public TempleOSRSError error;
}
