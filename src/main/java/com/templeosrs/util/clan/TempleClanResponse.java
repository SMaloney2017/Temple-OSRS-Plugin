package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TempleClanResponse
{
	@SerializedName("data")
	public TempleClanData data;

	@SerializedName("error")
	public TempleError error;
}
