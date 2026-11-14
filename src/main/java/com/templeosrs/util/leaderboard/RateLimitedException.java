package com.templeosrs.util.leaderboard;

import java.io.IOException;

/**
 * Thrown when TempleOSRS responds with HTTP 429 Too Many Requests.
 */
public class RateLimitedException extends IOException
{
	public RateLimitedException()
	{
		super("Rate limited by TempleOSRS");
	}
}
