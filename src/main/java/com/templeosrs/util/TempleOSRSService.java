package com.templeosrs.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TempleOSRSService
{
	public static final String HOST = "https://templeosrs.com/";

	public static final String PLAYER_PAGE = "player/overview.php?player=";

	public static final String CLAN_PAGE = "groups/overview.php?id=";

	private static final String PLAYER_OVERVIEW = "player/view/overview_skilling_view.php?player=";

	private static final String BOSSES = "&tracking=bosses";

	private static final String DURATION = "&duration=";

	private static final String CLAN_OVERVIEW = "api/group_info.php?id=";

	private static final String CLAN_ACHIEVEMENTS = "api/group_achievements.php?id=";

	private static final String CLAN_EDIT = "api/edit_group.php?";

	public static CompletableFuture<TempleOSRSPlayer> fetchUserGainsAsync(String player, String duration) throws Exception
	{
		String playerSkillsOverviewJSON = null;
		String playerBossingOverviewJSON = null;
		OkHttpClient client = new OkHttpClient();

		String playerSkillsOverviewURL = HOST + PLAYER_OVERVIEW + player + DURATION + duration;
		Request playerSkillsRequest = new Request.Builder()
			.url(playerSkillsOverviewURL)
			.build();

		Call playerSkillsCall = client.newCall(playerSkillsRequest);
		ResponseBody playerSkillsResponse = playerSkillsCall.execute().body();
		if(playerSkillsResponse != null)
		{
			playerSkillsOverviewJSON = playerSkillsResponse.string();
		}

		String playerBossingOverviewURL = HOST + PLAYER_OVERVIEW + player + BOSSES + DURATION + duration;
		Request playerBossingRequest = new Request.Builder()
			.url(playerBossingOverviewURL)
			.build();

		Call playerBossingCall = client.newCall(playerBossingRequest);
		ResponseBody playerBossingResponse = playerBossingCall.execute().body();
		if(playerBossingResponse != null)
		{
			playerBossingOverviewJSON = playerBossingResponse.string();
		}

		CompletableFuture<TempleOSRSPlayer> future = new CompletableFuture<>();
		future.complete(new TempleOSRSPlayer(playerSkillsOverviewJSON, playerBossingOverviewJSON));
		return future;
	}

	public static CompletableFuture<TempleOSRSClan> fetchClanAsync(String clanID) throws Exception
	{
		String clanOverviewJSON = null;
		String clanAchievementsJSON = null;
		OkHttpClient client = new OkHttpClient();

		String clanOverviewURL = HOST + CLAN_OVERVIEW + clanID;
		Request overviewRequest = new Request.Builder()
			.url(clanOverviewURL)
			.build();

		Call overviewCall = client.newCall(overviewRequest);
		ResponseBody overviewResponse = overviewCall.execute().body();
		if(overviewResponse != null)
		{
			clanOverviewJSON = overviewResponse.string();
		}

		String clanAchievementsURL = HOST + CLAN_ACHIEVEMENTS + clanID;
		Request achievementsRequest = new Request.Builder()
			.url(clanAchievementsURL)
			.build();

		Call achievementsCall = client.newCall(achievementsRequest);
		ResponseBody achievementsResponse = achievementsCall.execute().body();
		if(achievementsResponse != null)
		{
			clanAchievementsJSON = achievementsResponse.string();
		}

		CompletableFuture<TempleOSRSClan> future = new CompletableFuture<>();
		future.complete(new TempleOSRSClan(clanOverviewJSON, clanAchievementsJSON));
		return future;
	}

	public static CompletableFuture<Response> postClanMembersAsync(String clanID, String verification, List<String> members) throws Exception
	{
		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new FormBody.Builder()
			.add("id", clanID)
			.add("key", verification)
			.add("memberlist", String.valueOf(members))
			.build();

		Request request = new Request.Builder()
			.url(HOST + CLAN_EDIT)
			.post(formBody)
			.build();

		Call call = client.newCall(request);
		Response response = call.execute();

		CompletableFuture<Response> future = new CompletableFuture<>();
		future.complete(response);
		return future;
	}
}
