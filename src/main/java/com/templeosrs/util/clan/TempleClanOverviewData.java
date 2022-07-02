package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;

public class TempleClanOverviewData
{
	@SerializedName("info")
	public TempleClanOverviewInfo info;

	@SerializedName("members")
	public String[] members;

	@SerializedName("leaders")
	public String[] leaders;
}
