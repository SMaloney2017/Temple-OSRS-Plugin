package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TempleClanResponse
{
	@SerializedName("data")
	public TempleClanResponseData data;

	@SerializedName("error")
	public TempleError error;
}
