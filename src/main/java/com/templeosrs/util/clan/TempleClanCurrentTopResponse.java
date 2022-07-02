package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;
import java.util.Map;

public class TempleClanCurrentTopResponse
{
	@SerializedName("Week")
	public Map<String, TempleClanCurrentTopPlayer> week;

	@SerializedName("error")
	public TempleError error;
}
