package com.templeosrs.util;

import com.google.gson.annotations.SerializedName;

public class TempleError
{
	@SerializedName("code")
	public Integer code;

	@SerializedName("message")
	public String message;
}
