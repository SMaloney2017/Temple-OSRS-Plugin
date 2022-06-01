package com.templeosrs.util;

import com.google.gson.annotations.SerializedName;

public class TempleError
{
	@SerializedName("Code")
	public Integer code;

	@SerializedName("Message")
	public String message;
}
