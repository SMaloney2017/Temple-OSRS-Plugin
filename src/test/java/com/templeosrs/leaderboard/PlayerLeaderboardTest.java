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
	void combinedFiltersUseBuildEhpAndGameModeEhbWithoutRanks()
	{
		/* the issue's example: F2P Ultimate Ironman; TempleOSRS' primary EHP/EHB for such accounts are F2p_ehp/Uim_ehb */
		PlayerLeaderboard f2pUim = new PlayerLeaderboard(GameModeFilter.ULTIMATE_IRONMAN, AccountBuildFilter.F2P);

		/* F2P ironmen use their player-overview F2P EHP row; TempleOSRS' F2P ironman leaderboard includes every
		 * ironman type, so F2P UIM is not ranked */
		assertEquals("F2P UIM", f2pUim.getName());
		assertTrue(f2pUim.isF2pIronman());
		assertNull(f2pUim.getEhpMetric());
		assertNull(f2pUim.getEhpKey());
		assertEquals("Uim_ehb", f2pUim.getEhbKey());
		assertNull(f2pUim.getEhbMetric());

		PlayerLeaderboard f2pIronman = new PlayerLeaderboard(GameModeFilter.IRONMAN, AccountBuildFilter.F2P);
		assertTrue(f2pIronman.isF2pIronman());
		assertEquals("f2pim", f2pIronman.getEhpMetric());
		assertNull(f2pIronman.getEhbMetric());

		/* other combinations use the build's EHP without a rank */
		PlayerLeaderboard lvl3Ironman = new PlayerLeaderboard(GameModeFilter.IRONMAN, AccountBuildFilter.LVL_3);
		assertFalse(lvl3Ironman.isF2pIronman());
		assertEquals("Lvl3_ehp", lvl3Ironman.getEhpKey());
		assertNull(lvl3Ironman.getEhpMetric());

		PlayerLeaderboard f2pMain = new PlayerLeaderboard(GameModeFilter.MAIN, AccountBuildFilter.F2P);
		assertFalse(f2pMain.isF2pIronman());
		assertEquals("F2p_ehp", f2pMain.getEhpKey());
		assertNull(f2pMain.getEhpMetric());

		/* builds without their own rates have no EHP value */
		PlayerLeaderboard tenHpUim = new PlayerLeaderboard(GameModeFilter.ULTIMATE_IRONMAN, AccountBuildFilter.TEN_HITPOINTS);
		assertNull(tenHpUim.getEhpKey());
		assertEquals("Uim_ehb", tenHpUim.getEhbKey());
	}
}
