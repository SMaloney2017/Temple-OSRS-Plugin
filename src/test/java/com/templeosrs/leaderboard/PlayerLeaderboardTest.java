package com.templeosrs.leaderboard;

import com.templeosrs.util.leaderboard.AccountBuildFilter;
import com.templeosrs.util.leaderboard.GameModeFilter;
import com.templeosrs.util.leaderboard.PlayerLeaderboard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PlayerLeaderboardTest
{
	@Test
	void allUsesOverallLeaderboards()
	{
		PlayerLeaderboard all = PlayerLeaderboard.ALL;

		assertTrue(all.isAll());
		assertEquals("All", all.getName());
		assertEquals("ehp", all.getEhpMetric());
		assertEquals("Ehp", all.getEhpKey());
		assertEquals("ehb", all.getEhbMetric());
		assertEquals("Ehb", all.getEhbKey());
	}

	@Test
	void gameModeOnlyUsesGameModeLeaderboards()
	{
		PlayerLeaderboard ironman = new PlayerLeaderboard(GameModeFilter.IRONMAN, AccountBuildFilter.ALL);

		assertFalse(ironman.isAll());
		assertEquals("Ironman", ironman.getName());
		assertEquals("im", ironman.getEhpMetric());
		assertEquals("Im_ehp", ironman.getEhpKey());
		assertEquals("ehbim", ironman.getEhbMetric());
		assertEquals("Im_ehb", ironman.getEhbKey());

		PlayerLeaderboard uim = new PlayerLeaderboard(GameModeFilter.ULTIMATE_IRONMAN, AccountBuildFilter.ALL);
		assertEquals("uim", uim.getEhpMetric());
		assertEquals("Uim_ehb", uim.getEhbKey());
		assertNull(uim.getEhbMetric());
	}

	@Test
	void buildOnlyUsesBuildLeaderboards()
	{
		PlayerLeaderboard f2p = new PlayerLeaderboard(GameModeFilter.ALL, AccountBuildFilter.F2P);

		assertEquals("F2P", f2p.getName());
		assertEquals("f2p", f2p.getEhpMetric());
		assertEquals("F2p_ehp", f2p.getEhpKey());
		assertNull(f2p.getEhbMetric());
		assertNull(f2p.getEhbKey());

		PlayerLeaderboard lvl3 = new PlayerLeaderboard(GameModeFilter.ALL, AccountBuildFilter.LVL_3);
		assertEquals("lvl3", lvl3.getEhpMetric());
		assertEquals("Lvl3_ehp", lvl3.getEhpKey());
	}

	@Test
	void valuesWithoutLeaderboardsAreNotRanked()
	{
		PlayerLeaderboard hcim = new PlayerLeaderboard(GameModeFilter.HARDCORE_IRONMAN, AccountBuildFilter.ALL);
		assertEquals("Im_ehp", hcim.getEhpKey());
		assertNull(hcim.getEhpMetric());

		PlayerLeaderboard oneDef = new PlayerLeaderboard(GameModeFilter.ALL, AccountBuildFilter.ONE_DEFENCE);
		assertEquals("1def_ehp", oneDef.getEhpKey());
		assertEquals("1def_ehb", oneDef.getEhbKey());
		assertNull(oneDef.getEhpMetric());
		assertNull(oneDef.getEhbMetric());

		PlayerLeaderboard tenHp = new PlayerLeaderboard(GameModeFilter.ALL, AccountBuildFilter.TEN_HITPOINTS);
		assertNull(tenHp.getEhpKey());
		assertNull(tenHp.getEhbKey());
	}

	@Test
	void combinedFiltersAreNotTracked()
	{
		/* the issue's example: F2P Ultimate Ironman */
		PlayerLeaderboard f2pUim = new PlayerLeaderboard(GameModeFilter.ULTIMATE_IRONMAN, AccountBuildFilter.F2P);

		assertEquals("F2P UIM", f2pUim.getName());
		assertNull(f2pUim.getEhpMetric());
		assertNull(f2pUim.getEhpKey());
		assertNull(f2pUim.getEhbMetric());
		assertNull(f2pUim.getEhbKey());
	}
}
