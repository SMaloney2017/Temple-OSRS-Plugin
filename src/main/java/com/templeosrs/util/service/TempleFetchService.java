package com.templeosrs.util.service;

import com.templeosrs.util.clan.TempleClan;
import com.templeosrs.util.comp.TempleCompetition;
import com.templeosrs.util.player.TemplePlayer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TempleFetchService
{
	public static final String HOST = "https://templeosrs.com/";

	public static final String PLAYER_PAGE = "player/overview.php?player=";

	public static final String CLAN_PAGE = "groups/overview.php?id=";

	public static final String COMPETITION_PAGE = "/competitions/standings.php?id=";

	private static final String PLAYER_OVERVIEW = "player/view/overview_skilling_view.php?player=";

	private static final String BOSSES = "&tracking=bosses";

	private static final String DURATION = "&duration=";

	private static final String CLAN_OVERVIEW = "api/group_info.php?id=";

	private static final String CLAN_ACHIEVEMENTS = "api/group_achievements.php?id=";

	private static final String CLAN_EDIT = "api/edit_group.php?";

	private static final String COMPETITION = "api/competition_info.php?id=";

	private static String fetchData(String URL) throws Exception
	{
		String JSON = null;
		OkHttpClient client = new OkHttpClient();

		Request overviewRequest = new Request.Builder()
			.url(URL)
			.build();

		Call call = client.newCall(overviewRequest);
		ResponseBody response = call.execute().body();
		if (response != null)
		{
			JSON = response.string();
			response.close();
		}

		return JSON;
	}

	public static CompletableFuture<TemplePlayer> fetchUserGainsAsync(String player, String duration) throws Exception
	{
		String playerSkillsOverviewJSON = fetchData(HOST + PLAYER_OVERVIEW + player + DURATION + duration);
		String playerBossingOverviewJSON = fetchData(HOST + PLAYER_OVERVIEW + player + BOSSES + DURATION + duration);

		CompletableFuture<TemplePlayer> future = new CompletableFuture<>();
		future.complete(new TemplePlayer(playerSkillsOverviewJSON, playerBossingOverviewJSON));
		return future;
	}

	public static CompletableFuture<TempleClan> fetchClanAsync(String clanID) throws Exception
	{
		String clanOverviewJSON = fetchData(HOST + CLAN_OVERVIEW + clanID);
		String clanAchievementsJSON = fetchData(HOST + CLAN_ACHIEVEMENTS + clanID);

		CompletableFuture<TempleClan> future = new CompletableFuture<>();
		future.complete(new TempleClan(clanOverviewJSON, clanAchievementsJSON));
		return future;
	}

	public static CompletableFuture<TempleCompetition> fetchCompetitionAsync(String competitionID) throws Exception
	{
		String competitionJSON = fetchData(HOST + COMPETITION + competitionID);
		CompletableFuture<TempleCompetition> future = new CompletableFuture<>();

		future.complete(new TempleCompetition(competitionJSON));
		return future;
	}

	public static CompletableFuture<TempleSyncResponse> postClanMembersAsync(String clanID, String verification, List<String> members) throws Exception
	{
		String JSON = null;
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
		ResponseBody body = response.body();

		if (body != null)
		{
			JSON = body.string();
			response.close();
		}

		CompletableFuture<TempleSyncResponse> future = new CompletableFuture<>();
		future.complete(new TempleSyncResponse(JSON));
		return future;
	}
}
