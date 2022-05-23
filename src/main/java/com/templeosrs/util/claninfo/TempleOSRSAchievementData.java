package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleOSRSError;
import java.util.List;

public class TempleOSRSAchievementData
{
	@SerializedName("data")
	public List<TempleOSRSClanAchievementSkill> data = null;

	@SerializedName("error")
	public TempleOSRSError error;
}
