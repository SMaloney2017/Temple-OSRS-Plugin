package com.templeosrs.util.sync;

import com.google.gson.annotations.SerializedName;
import com.templeosrs.util.TempleError;

public class TempleSyncResponse
{
	@SerializedName("data")
	public TempleSyncResponseData data;

	@SerializedName("error")
	public TempleError error;
}
