package com.templeosrs.util.leaderboard;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.templeosrs.util.player.TemplePlayerSkill;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Looks up a player's EHP / EHB ranks on filtered TempleOSRS leaderboards.
 * <p>
 * Each uncached rank costs roughly 18 requests (see {@link LeaderboardRankFinder}), so both rank thresholds and
 * finished results are cached. Rank thresholds near the top of the binary search are shared between players.
 */
@Slf4j
@Singleton
public class LeaderboardService
{
	private static final long CACHE_MINUTES = 10;

	private final OkHttpClient client;

	private final Gson gson;

	/* "metric:rank" -> threshold; Optional.empty() when no player holds that rank */
	private final Cache<String, Optional<Double>> thresholds = CacheBuilder.newBuilder()
		.expireAfterWrite(CACHE_MINUTES, TimeUnit.MINUTES)
		.maximumSize(5000)
		.build();

	/* "player:leaderboard" -> stats */
	private final Cache<String, LeaderboardStats> results = CacheBuilder.newBuilder()
		.expireAfterWrite(CACHE_MINUTES, TimeUnit.MINUTES)
		.maximumSize(200)
		.build();

	@Inject
	public LeaderboardService(OkHttpClient client, Gson gson)
	{
		this.client = client;
		this.gson = gson;
	}

	/**
	 * Blocking; must not be called on the client or Swing threads.
	 *
	 * @param player   username formatted for the TempleOSRS API
	 * @param overview the player's player-overview table, which holds values missing from api/player_stats.php
	 * @param progress receives partial results (totals first, then each rank as it is found) before this returns
	 * @return the final result; ranks which could not be determined are null
	 * @throws IOException if the player's stats can not be fetched
	 */
	public LeaderboardStats fetchStats(String player, PlayerLeaderboard leaderboard, Map<String, TemplePlayerSkill> overview, Consumer<LeaderboardStats> progress) throws IOException
	{
		String key = player.toLowerCase() + ":" + leaderboard.getGameMode().name() + ":" + leaderboard.getBuild().name();

		LeaderboardStats cached = results.getIfPresent(key);
		if (cached != null)
		{
			return cached;
		}

		if (leaderboard.getEhpKey() == null && leaderboard.getEhbKey() == null && !leaderboard.isF2pIronman())
		{
			/* TempleOSRS tracks nothing for this game mode / build combination */
			return new LeaderboardStats(leaderboard, null, null, null, null, true, false);
		}

		JsonObject stats = requestPlayerStats(player);

		Double ehp = leaderboard.isF2pIronman() ? getF2pIronmanEhp(stats, leaderboard, overview) : getValue(stats, leaderboard.getEhpKey());
		Double ehb = getValue(stats, leaderboard.getEhbKey());

		LeaderboardStats result = new LeaderboardStats(leaderboard, ehp, null, ehb, null, false, false);

		try
		{
			if (ehp != null && leaderboard.getEhpMetric() != null)
			{
				progress.accept(result);
				result = result.withEhpRank(LeaderboardRankFinder.findRank(ehp, rank -> threshold(leaderboard.getEhpMetric(), rank)));
			}

			if (ehb != null && leaderboard.getEhbMetric() != null)
			{
				progress.accept(result);
				result = result.withEhbRank(LeaderboardRankFinder.findRank(ehb, rank -> threshold(leaderboard.getEhbMetric(), rank)));
			}

			result = result.withComplete(true);
			results.put(key, result);
		}
		catch (RateLimitedException e)
		{
			log.debug("Rate limited while looking up {} leaderboard ranks for {}", leaderboard, player);
			result = result.withRateLimited(true);
		}
		catch (IOException e)
		{
			log.debug("Unable to determine {} leaderboard ranks for {}", leaderboard, player, e);
		}

		return result;
	}

	private JsonObject requestPlayerStats(String player) throws IOException
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("player_stats.php").addQueryParameter("player", player).addQueryParameter("bosses", "1").build();

		JsonObject response = request(url);

		if (response.has("error") || !response.has("data") || !response.get("data").isJsonObject())
		{
			throw new IOException("Unable to fetch player stats for " + player);
		}

		return response.getAsJsonObject("data");
	}

	private Double threshold(String metric, int rank) throws IOException
	{
		String key = metric + ":" + rank;

		Optional<Double> cached = thresholds.getIfPresent(key);
		if (cached != null)
		{
			return cached.orElse(null);
		}

		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("ehp_by_rank.php").addQueryParameter("rank", String.valueOf(rank)).addQueryParameter("metric", metric).build();

		JsonObject response = request(url);

		Double threshold = null;
		if (response.has("data") && response.get("data").isJsonObject())
		{
			JsonElement value = response.getAsJsonObject("data").get("rank_threshold");
			if (value != null && value.isJsonPrimitive())
			{
				threshold = value.getAsDouble();
			}
		}

		thresholds.put(key, Optional.ofNullable(threshold));

		return threshold;
	}

	private JsonObject request(HttpUrl url) throws IOException
	{
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute())
		{
			if (response.code() == 429)
			{
				throw new RateLimitedException();
			}

			ResponseBody body = response.body();
			if (!response.isSuccessful() || body == null)
			{
				throw new IOException("Unexpected response " + response.code() + " from " + url.encodedPath());
			}

			JsonObject json = gson.fromJson(body.charStream(), JsonObject.class);
			if (json == null)
			{
				throw new IOException("Empty response from " + url.encodedPath());
			}

			return json;
		}
	}

	/* 0 unless the player is an F2P ironman of the leaderboard's game mode */
	private static double getF2pIronmanEhp(JsonObject stats, PlayerLeaderboard leaderboard, Map<String, TemplePlayerSkill> overview)
	{
		JsonObject info = stats.has("info") && stats.get("info").isJsonObject() ? stats.getAsJsonObject("info") : new JsonObject();
		JsonElement gameMode = info.get("Game mode");
		JsonElement f2p = info.get("F2p");

		boolean isF2pIronman = gameMode != null && gameMode.isJsonPrimitive() && leaderboard.getGameMode().matches(gameMode.getAsInt())
			&& gameMode.getAsInt() != 0 && f2p != null && f2p.isJsonPrimitive() && f2p.getAsInt() == 1;

		TemplePlayerSkill row = overview != null ? overview.get(PlayerLeaderboard.F2P_IRONMAN_EHP_ROW) : null;

		if (!isF2pIronman || row == null || row.xpTotal == null)
		{
			return 0;
		}

		return row.xpTotal;
	}

	/* null if the key is null, i.e. TempleOSRS tracks no such value */
	private static Double getValue(JsonObject stats, String key)
	{
		if (key == null)
		{
			return null;
		}

		JsonElement value = stats.get(key);

		if (value == null || !value.isJsonPrimitive())
		{
			return 0d;
		}

		try
		{
			return value.getAsDouble();
		}
		catch (NumberFormatException e)
		{
			return 0d;
		}
	}
}
