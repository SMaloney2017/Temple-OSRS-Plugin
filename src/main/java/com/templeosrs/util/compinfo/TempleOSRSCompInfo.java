package com.templeosrs.util.compinfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSCompInfo
{
	@SerializedName("id")
	public Integer id;

	@SerializedName("name")
	public String name;

	@SerializedName("team_competition")
	public Boolean teamCompetition;

	@SerializedName("participant_count")
	public Integer participantCount;

	@SerializedName("skill")
	public String skill;

	@SerializedName("skill_index")
	public Integer skillIndex;

	@SerializedName("start_date")
	public String startDate;

	@SerializedName("end_date")
	public String endDate;

	@SerializedName("status")
	public Integer status;

	@SerializedName("status_text")
	public String statusText;
}
