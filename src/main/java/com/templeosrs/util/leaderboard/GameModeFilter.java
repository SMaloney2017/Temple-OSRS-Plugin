package com.templeosrs.util.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Game mode filter for the player overview, matching the TempleOSRS virtual hiscores.
 */
@Getter
@AllArgsConstructor
public enum GameModeFilter
{
	ALL("All", "ehp", "Ehp", "ehb", "Ehb"),
	MAIN("Main", null, "Ehp", null, "Ehb"),
	/* TempleOSRS' ironman leaderboards include hardcore and ultimate ironmen */
	IRONMAN("Ironman", "im", "Im_ehp", "ehbim", "Im_ehb"),
	HARDCORE_IRONMAN("HCIM", null, "Im_ehp", null, "Im_ehb"),
	ULTIMATE_IRONMAN("UIM", "uim", "Uim_ehp", null, "Uim_ehb"),
	GROUP_IRONMAN("GIM", null, "Gim_ehp", null, null);

	private final String name;

	/* metric parameter for api/ehp_by_rank.php; null if TempleOSRS exposes no such leaderboard */
	private final String ehpMetric;

	/* key of the matching value in api/player_stats.php; null if TempleOSRS tracks no such value */
	private final String ehpKey;

	private final String ehbMetric;

	private final String ehbKey;

	@Override
	public String toString()
	{
		return name;
	}
}
