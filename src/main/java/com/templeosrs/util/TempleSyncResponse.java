package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.claninfo.TempleClanResponse;
import java.util.Objects;

public class TempleSyncResponse
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleClanResponse errorResponse;

	TempleSyncResponse(String response)
	{
		errorResponse = gson.fromJson(response, TempleClanResponse.class);

		if (Objects.nonNull(errorResponse.error))
		{
			error = true;
		}
	}
}
