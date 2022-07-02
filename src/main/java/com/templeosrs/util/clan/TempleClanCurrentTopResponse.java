package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;
import java.util.Map;

public class TempleClanCurrentTopResponse
{
	@SerializedName(value = "Day", alternate = {"Week", "Month"})
	public Map<String, TempleClanCurrentTopPlayer> list;

	@SerializedName("error")
	public TempleError error;
}
