package com.templeosrs.util;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayerRanges
{
	Day("Day", "1day"),
	Week("Week", "1week"),
	Month("Month", "31day"),
	Six_Months("Six Months", "186days"),
	Year("Year", "365days"),
	All_Time("All Time", "alltime");

	private static final Map<String, PlayerRanges> lookup = new HashMap<>();

	static
	{
		for (PlayerRanges r : PlayerRanges.values())
		{
			lookup.put(r.getName(), r);
		}
	}

	private final String name;
	private final String range;

	public static PlayerRanges get(String name)
	{
		return lookup.get(name);
	}

	public String getName()
	{
		return this.name;
	}

}
