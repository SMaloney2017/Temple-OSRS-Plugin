package com.templeosrs.util.leaderboard;

import lombok.NonNull;
import lombok.Value;

/**
 * A TempleOSRS virtual leaderboard selected by game mode and account build, which the player overview's EHP and EHB
 * are shown for.
 * <p>
 * TempleOSRS only exposes some of these leaderboards through its API (api/ehp_by_rank.php), so a rank can only be
 * determined when {@link #getEhpMetric()} / {@link #getEhbMetric()} is not null, see {@link LeaderboardRankFinder}.
 * Likewise a value is only shown when TempleOSRS tracks one for the game mode / build (api/player_stats.php).
 */
@Value
public class PlayerLeaderboard
{
	public static final PlayerLeaderboard ALL = new PlayerLeaderboard(GameModeFilter.ALL, AccountBuildFilter.ALL);

	@NonNull
	GameModeFilter gameMode;

	@NonNull
	AccountBuildFilter build;

	public boolean isAll()
	{
		return gameMode == GameModeFilter.ALL && build == AccountBuildFilter.ALL;
	}

	public String getName()
	{
		if (isAll())
		{
			return "All";
		}

		if (build == AccountBuildFilter.ALL)
		{
			return gameMode.getName();
		}

		if (gameMode == GameModeFilter.ALL)
		{
			return build.getName();
		}

		return build.getName() + " " + gameMode.getName();
	}

	public String getEhpMetric()
	{
		return select(gameMode.getEhpMetric(), build.getEhpMetric());
	}

	public String getEhpKey()
	{
		return select(gameMode.getEhpKey(), build.getEhpKey());
	}

	public String getEhbMetric()
	{
		return select(gameMode.getEhbMetric(), build.getEhbMetric());
	}

	public String getEhbKey()
	{
		return select(gameMode.getEhbKey(), build.getEhbKey());
	}

	/* TempleOSRS' API exposes values and leaderboards filtered by either game mode or build, but not both */
	private String select(String gameModeValue, String buildValue)
	{
		if (build == AccountBuildFilter.ALL)
		{
			return gameModeValue;
		}

		if (gameMode == GameModeFilter.ALL)
		{
			return buildValue;
		}

		return null;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
