package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;

public class TemplePlayerInfo
{
	@SerializedName("name")
	public String name;

	@SerializedName("tracking_start")
	public String trackingStart;

	@SerializedName("tracking_end")
	public String trackingEnd;

	@SerializedName("tracking_type")
	public String trackingType;

	@SerializedName("tracking_ehp_index")
	public String trackingEhpIndex;

	@SerializedName("tracking_length_text")
	public String trackingLengthText;

	@SerializedName("last_check")
	public String lastCheck;

	@SerializedName("last_change")
	public String lastChange;

	@SerializedName("last_check_text")
	public String lastCheckText;

	@SerializedName("last_change_text")
	public String lastChangeText;

	@SerializedName("xp_drop")
	public String xpDrop;

	@SerializedName("xp_drop_text")
	public String xpDropText;

	@SerializedName("earliest")
	public String earliest;

	@SerializedName("earliest_text")
	public String earliestText;

	@SerializedName("total_dp_count")
	public String totalDpCount;

	@SerializedName("cooldown")
	public String cooldown;

	@SerializedName("days")
	public String days;

	@SerializedName("hours_played")
	public String hoursPlayed;

	@SerializedName("hours_per_day")
	public String hoursPerDay;

	@SerializedName("xp_gained")
	public String xpGained;

	@SerializedName("xp_per_day")
	public String xpPerDay;

	@SerializedName("avr_xph")
	public String avrXph;

	@SerializedName("top_skill")
	public String topSkill;

	@SerializedName("gp_spent")
	public String gpSpent;
}
