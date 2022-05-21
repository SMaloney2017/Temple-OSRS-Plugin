package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TempleOSRSPlayerGraph
{
	@SerializedName("skill")
	public String skill;

	@SerializedName("skill_index")
	public Integer skillIndex;

	@SerializedName("xp")
	public List<Double> xp = null;

	@SerializedName("rank")
	public List<Double> rank = null;

	@SerializedName("date")
	public List<String> date = null;
}