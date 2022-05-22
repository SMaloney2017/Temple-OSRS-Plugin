package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSClanAchievementSkill
{
	@SerializedName("username")
	public String username;

	@SerializedName("date")
	public String date;

	@SerializedName("skill")
	public String skill;

	@SerializedName("type")
	public String type;

	@SerializedName("xp")
	public Long xp;
}

