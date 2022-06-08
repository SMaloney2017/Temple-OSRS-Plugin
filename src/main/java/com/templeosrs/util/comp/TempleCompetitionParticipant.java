package com.templeosrs.util.comp;

import com.google.gson.annotations.SerializedName;

public class TempleCompetitionParticipant
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

	@SerializedName("team_name")
	public String teamName;
}
