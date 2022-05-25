package com.templeosrs.util.player;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TemplePlayerResponse
{
	@SerializedName("data")
	public TemplePlayerData data;

	@SerializedName("error")
	public TempleError error;
}
