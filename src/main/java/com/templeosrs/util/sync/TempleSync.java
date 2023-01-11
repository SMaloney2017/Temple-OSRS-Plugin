package com.templeosrs.util.sync;

import com.google.gson.Gson;
import java.util.Objects;

public class TempleSync
{
	public boolean error = false;

	public TempleSyncResponse dataResponse;

	public TempleSync(String response, Gson gson)
	{
		dataResponse = gson.fromJson(response, TempleSyncResponse.class);

		if (Objects.nonNull(dataResponse.error))
		{
			error = true;
		}
	}
}
