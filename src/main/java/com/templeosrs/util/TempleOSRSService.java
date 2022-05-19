package com.templeosrs.util;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TempleOSRSService
{
	public static final String HOST = "https://templeosrs.com/";

	public static final String PLAYER_PAGE = "player/overview.php?player=";

	private static final String PLAYER_OVERVIEW = "/player/view/overview_skilling_view.php?player=";

	private static final String BOSSES = "&tracking=bosses";

	private static final String DURATION = "&duration=";

	private static String getJsonFromURL(String urlString) throws IOException
	{
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
			.url(urlString)
			.build();

		Response response = client.newCall(request).execute();

		if (response.body() != null)
		{
			return response.body().string();
		}
		return null;
	}

	public static CompletableFuture<TempleOSRSPlayer> fetchUserGainsAsync(String player, String duration) throws Exception
	{
		String playerSkillsOverviewURL = HOST + PLAYER_OVERVIEW + player + DURATION + duration;
		String playerBossingOverviewURL = HOST + PLAYER_OVERVIEW + player + BOSSES + DURATION + duration;

		String playerSkillsOverviewJSON;
		String playerBossingOverviewJSON;

		playerSkillsOverviewJSON = getJsonFromURL(playerSkillsOverviewURL);
		playerBossingOverviewJSON = getJsonFromURL(playerBossingOverviewURL);

		CompletableFuture<TempleOSRSPlayer> future = new CompletableFuture<>();
		future.complete(new TempleOSRSPlayer(playerSkillsOverviewJSON, playerBossingOverviewJSON));
		return future;
	}
}
