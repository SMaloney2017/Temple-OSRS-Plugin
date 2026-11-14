package com.templeosrs.leaderboard;

import com.templeosrs.util.leaderboard.LeaderboardRankFinder;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LeaderboardRankFinderTest
{
	/* leaderboard where the player at rank r holds MAX_RANK + 1 - r, i.e. rank 1 holds 99999 and rank 99999 holds 1 */
	private static Double linear(int rank)
	{
		return (double) (LeaderboardRankFinder.MAX_RANK + 1 - rank);
	}

	@Test
	void findsExactRank() throws IOException
	{
		assertEquals(1, LeaderboardRankFinder.findRank(99999, LeaderboardRankFinderTest::linear));
		assertEquals(75, LeaderboardRankFinder.findRank(99925, LeaderboardRankFinderTest::linear));
		assertEquals(LeaderboardRankFinder.MAX_RANK, LeaderboardRankFinder.findRank(1, LeaderboardRankFinderTest::linear));
	}

	@Test
	void valueBetweenThresholdsTakesNextRank() throws IOException
	{
		/* rank 75 holds 99925, rank 76 holds 99924 */
		assertEquals(76, LeaderboardRankFinder.findRank(99924.5, LeaderboardRankFinderTest::linear));
	}

	@Test
	void valueAboveFirstPlaceIsRankOne() throws IOException
	{
		assertEquals(1, LeaderboardRankFinder.findRank(1_000_000, LeaderboardRankFinderTest::linear));
	}

	@Test
	void tiesShareBestRank() throws IOException
	{
		/* ranks 1 and 2 are tied */
		assertEquals(1, LeaderboardRankFinder.findRank(500, rank -> rank <= 2 ? 500d : 500d - rank));
	}

	@Test
	void belowLastRankIsUnranked() throws IOException
	{
		assertEquals(LeaderboardRankFinder.UNRANKED, LeaderboardRankFinder.findRank(0.5, LeaderboardRankFinderTest::linear));
	}

	@Test
	void zeroValueIsUnrankedWithoutRequests() throws IOException
	{
		AtomicInteger requests = new AtomicInteger();

		assertEquals(LeaderboardRankFinder.UNRANKED, LeaderboardRankFinder.findRank(0, rank -> {
			requests.incrementAndGet();
			return linear(rank);
		}));
		assertEquals(0, requests.get());
	}

	@Test
	void missingThresholdsAreTreatedAsEmptyRanks() throws IOException
	{
		/* only 1000 players on the leaderboard */
		LeaderboardRankFinder.ThresholdLookup small = rank -> rank <= 1000 ? (double) (1001 - rank) : null;

		assertEquals(1001, LeaderboardRankFinder.findRank(0.5, small));
		assertEquals(500, LeaderboardRankFinder.findRank(501, small));
	}

	@Test
	void usesLogarithmicNumberOfRequests() throws IOException
	{
		AtomicInteger requests = new AtomicInteger();

		LeaderboardRankFinder.findRank(12345, rank -> {
			requests.incrementAndGet();
			return linear(rank);
		});

		assertTrue(requests.get() <= 18, "expected at most 18 requests but made " + requests.get());
	}

	@Test
	void propagatesLookupFailures()
	{
		assertThrows(IOException.class, () -> LeaderboardRankFinder.findRank(10, rank -> {
			throw new IOException("rate limited");
		}));
	}
}
