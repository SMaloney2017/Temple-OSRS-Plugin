package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;

public class TempleClanData
{
	@SerializedName("info")
	public TempleClanInfo info;

	@SerializedName("members")
	public String[] members;

	@SerializedName("leaders")
	public String[] leaders;
}
