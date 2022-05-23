package com.templeosrs.util.compinfo;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleOSRSError;

public class TempleOSRSComp
{
	@SerializedName("data")
	public TempleOSRSCompData data;

	@SerializedName("error")
	public TempleOSRSError error;
}
