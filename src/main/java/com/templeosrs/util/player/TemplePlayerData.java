package com.templeosrs.util.player;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class TemplePlayerData
{
	@SerializedName("info")
	public TemplePlayerInfo info;

	@SerializedName("table")
	public Map<String, TemplePlayerSkill> table;

	@SerializedName("graph")
	public TemplePlayerGraph graph;
}
