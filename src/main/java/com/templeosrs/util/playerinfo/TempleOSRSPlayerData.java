package com.templeosrs.util.playerinfo;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class TempleOSRSPlayerData
{
	@SerializedName("info")
	public TempleOSRSPlayerInfo info;

	@SerializedName("table")
	public Map<String, TempleOSRSPlayerSkill> table;

	@SerializedName("graph")
	public TempleOSRSPlayerGraph graph;
}
