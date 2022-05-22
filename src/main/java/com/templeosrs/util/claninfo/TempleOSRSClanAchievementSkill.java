package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSClanAchievementSkill
{
	@SerializedName("Username")
	public String username;

	@SerializedName("Date")
	public String date;

	@SerializedName("Skill")
	public String skill;

	@SerializedName("Type")
	public String type;

	@SerializedName("Xp")
	public Long xp;
}

