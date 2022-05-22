package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.claninfo.TempleOSRSResponse;
import java.util.Objects;

public class TempleOSRSSync
{
	private final static Gson gson = new Gson();

	public boolean error = false;

	public TempleOSRSResponse errorResponse;

	TempleOSRSSync(String response)
	{
		errorResponse = gson.fromJson(response, TempleOSRSResponse.class);

		if (Objects.nonNull(errorResponse.error))
		{
			error = true;
		}
	}
}
