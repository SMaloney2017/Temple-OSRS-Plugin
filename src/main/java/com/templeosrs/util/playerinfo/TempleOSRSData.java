package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class TempleOSRSData
{
	@SerializedName("info")
	public TempleOSRSInfo info;

	@SerializedName("table")
	public Map<String, TempleOSRSSkill> table;

	@SerializedName("graph")
	public TempleOSRSGraph graph;
}
