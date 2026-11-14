package com.templeosrs.util.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Account build filter for the player overview, matching the TempleOSRS virtual hiscores.
 */
@Getter
@AllArgsConstructor
public enum AccountBuildFilter
{
	ALL("All", "ehp", "Ehp", "ehb", "Ehb"),
	F2P("F2P", "f2p", "F2p_ehp", null, null),
	LVL_3("Lvl-3", "lvl3", "Lvl3_ehp", null, null),
	ONE_DEFENCE("1 Def", null, "1def_ehp", null, "1def_ehb"),
	TEN_HITPOINTS("10 HP", null, null, null, null),
	ONE_DEFENCE_NO_LVL_3("1 Def (no Lvl-3)", null, null, null, null);

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
