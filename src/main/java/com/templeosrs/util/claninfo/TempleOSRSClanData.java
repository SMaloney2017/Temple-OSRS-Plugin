package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSClanData
{
	@SerializedName("info")
	public TempleOSRSClanInfo info;

	@SerializedName("members")
	public String[] members;

	@SerializedName("leaders")
	public String[] leaders;
}
