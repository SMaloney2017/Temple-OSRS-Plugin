package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;
import java.util.List;

public class TempleClanAchievementResponse
{
	@SerializedName("data")
	public List<TempleClanAchievement> data = null;

	@SerializedName("error")
	public TempleError error;
}
