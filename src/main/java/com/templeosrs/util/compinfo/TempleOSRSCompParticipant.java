package com.templeosrs.util.compinfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSCompParticipant
{
	@SerializedName("username")
	public String username;

	@SerializedName("xp_gained")
	public Double xpGained;

	@SerializedName("start_xp")
	public Double startXp;

	@SerializedName("end_xp")
	public Double endXp;

	@SerializedName("start_level")
	public Double startLevel;

	@SerializedName("current_level")
	public Double currentLevel;
}
