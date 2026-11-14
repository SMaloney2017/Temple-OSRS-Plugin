package com.templeosrs.util.leaderboard;

import java.io.IOException;

/**
 * Finds a player's rank on a TempleOSRS virtual leaderboard.
 * <p>
 * The TempleOSRS API does not expose a player's rank on filtered (game mode / account build) leaderboards,
 * but it does expose the value held by the player at any given rank (api/ehp_by_rank.php). Because the
 * leaderboard is sorted in descending order, the player's rank can be found with a binary search over
 * those thresholds.
 */
public final class LeaderboardRankFinder
{
	/**
	 * Highest rank exposed by api/ehp_by_rank.php; larger ranks are clamped to this value by the API.
	 */
	public static final int MAX_RANK = 99999;

	/**
	 * Rank returned when the player is not on the leaderboard (no value, or below {@link #MAX_RANK}).
	 */
	public static final int UNRANKED = 0;

	@FunctionalInterface
	public interface ThresholdLookup
	{
		/**
		 * @return the value held by the player at {@code rank}, or {@code null} if no player holds that rank
		 */
		Double thresholdAt(int rank) throws IOException;
	}

	private LeaderboardRankFinder()
	{
	}

	/**
	 * @return the best (lowest) rank whose threshold is less than or equal to {@code value},
	 * or {@link #UNRANKED} if the player does not place within {@link #MAX_RANK}
	 */
	public static int findRank(double value, ThresholdLookup lookup) throws IOException
	{
		if (value <= 0)
		{
			return UNRANKED;
		}

		if (!isAtOrBelow(lookup.thresholdAt(MAX_RANK), value))
		{
			return UNRANKED;
		}

		int low = 1;
		int high = MAX_RANK;

		while (low < high)
		{
			int mid = low + (high - low) / 2;

			if (isAtOrBelow(lookup.thresholdAt(mid), value))
			{
				high = mid;
			}
			else
			{
				low = mid + 1;
			}
		}

		return low;
	}

	/* a missing threshold means no player holds that rank, so the player would place above it */
	private static boolean isAtOrBelow(Double threshold, double value)
	{
		return threshold == null || threshold <= value;
	}
}
