package com.templeosrs.util.compinfo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TempleOSRSCompData
{
	@SerializedName("info")
	public TempleOSRSCompInfo info;

	@SerializedName("participants")
	public List<TempleOSRSCompParticipant> participants = null;
}
