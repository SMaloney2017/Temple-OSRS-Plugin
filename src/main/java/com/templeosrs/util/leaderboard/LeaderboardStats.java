package com.templeosrs.util.leaderboard;

import lombok.Value;
import lombok.With;

/**
 * A player's EHP / EHB totals and ranks on a {@link PlayerLeaderboard}.
 */
@Value
@With
public class LeaderboardStats
{
	PlayerLeaderboard leaderboard;

	/* null if TempleOSRS tracks no EHP value for the leaderboard */
	Double ehp;

	/* null until determined, if it could not be determined, or if the leaderboard has no EHP ranking;
	 * LeaderboardRankFinder.UNRANKED if unranked */
	Integer ehpRank;

	/* null if TempleOSRS tracks no EHB value for the leaderboard */
	Double ehb;

	/* null until determined, if it could not be determined, or if the leaderboard has no EHB ranking */
	Integer ehbRank;

	/* true once every rank lookup has finished successfully */
	boolean complete;

	/* rank lookups were interrupted because TempleOSRS rate limited the requests */
	boolean rateLimited;
}
