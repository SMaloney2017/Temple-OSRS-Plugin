package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;
import java.util.List;

public class TempleClanAchievementData
{
	@SerializedName("data")
	public List<TempleClanAchievement> data = null;

	@SerializedName("error")
	public TempleError error;
}
