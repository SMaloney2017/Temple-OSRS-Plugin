package com.templeosrs.util.comp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TempleCompetitionData
{
	@SerializedName("info")
	public TempleCompetitionInfo info;

	@SerializedName("participants")
	public List<TempleCompetitionParticipant> participants = null;
}
