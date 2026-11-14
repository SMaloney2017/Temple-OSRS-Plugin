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

	/**
	 * F2P ironmen (of every type) use F2P ironman EHP rates. Their F2P ironman EHP is not part of api/player_stats.php,
	 * but is the player's primary EHP row ({@link #F2P_IRONMAN_EHP_ROW}) in the player-overview response.
	 */
	public boolean isF2pIronman()
	{
		return build == AccountBuildFilter.F2P && gameMode.isIronman();
	}

	/* player-overview table row holding an F2P ironman's F2P ironman EHP */
	public static final String F2P_IRONMAN_EHP_ROW = "f2p_ehp";

	public String getEhpMetric()
	{
		if (isF2pIronman())
		{
			/* the F2P ironman leaderboard includes every ironman type, so it can't rank F2P HCIM / UIM on their own */
			return gameMode == GameModeFilter.IRONMAN ? "f2pim" : null;
		}

		return select(gameMode.getEhpMetric(), build.getEhpMetric(), null);
	}

	/* when filtering by both, EHP uses the build's rates, matching TempleOSRS' primary EHP (e.g. F2p_ehp for a F2P UIM) */
	public String getEhpKey()
	{
		return isF2pIronman() ? null : select(gameMode.getEhpKey(), build.getEhpKey(), build.getEhpKey());
	}

	public String getEhbMetric()
	{
		return select(gameMode.getEhbMetric(), build.getEhbMetric(), null);
	}

	/* when filtering by both, EHB uses the game mode's rates, matching TempleOSRS' primary EHB (e.g. Uim_ehb for a F2P UIM) */
	public String getEhbKey()
	{
		return select(gameMode.getEhbKey(), build.getEhbKey(), gameMode.getEhbKey());
	}

	/* TempleOSRS' API only exposes leaderboards filtered by either game mode or build, so combined filters are never ranked */
	private String select(String gameModeValue, String buildValue, String combinedValue)
	{
		if (build == AccountBuildFilter.ALL)
		{
			return gameModeValue;
		}

		if (gameMode == GameModeFilter.ALL)
		{
			return buildValue;
		}

		return combinedValue;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
