package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleOSRSError;

public class TempleOSRSResponse
{
	@SerializedName("data")
	public TempleOSRSResponseData data;

	@SerializedName("error")
	public TempleOSRSError error;
}
