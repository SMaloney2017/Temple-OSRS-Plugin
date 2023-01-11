package com.templeosrs.util;

import com.google.gson.Gson;
import com.templeosrs.util.clan.TempleClan;
import com.templeosrs.util.comp.TempleCompetition;
import com.templeosrs.util.player.TemplePlayer;
import com.templeosrs.util.sync.TempleSync;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TempleService
{
	@Inject
	private OkHttpClient client;

	@Inject
	private Gson gson;

	private String request(Request request) throws Exception
	{
		String JSON = null;

		Call call = client.newCall(request);
		Response response = call.execute();
		ResponseBody body = response.body();

		if (body != null)
		{
			JSON = body.string();
			response.close();
		}

		return JSON;
	}

	public String requestUserSkillGains(String player, String duration) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("player").addPathSegment("view").addPathSegment("overview_skilling_view.php").addQueryParameter("player", player).addQueryParameter("duration", duration).build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public String requestUserBossGains(String player, String duration) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("player").addPathSegment("view").addPathSegment("overview_skilling_view.php").addQueryParameter("player", player).addQueryParameter("duration", duration).addQueryParameter("tracking", "bosses").build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public String requestClanOverview(String id) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("group_info.php").addQueryParameter("id", id).build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public String requestClanAchievements(String id) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("group_achievements.php").addQueryParameter("id", id).build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public String requestCompetitionInfo(String id) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("competition_info.php").addQueryParameter("id", id).build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public String requestClanCurrentTop(String skill, String id, String range) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("current_top").addPathSegment(range + ".php").addQueryParameter("skill", skill).addQueryParameter("group", id).build();

		Request request = new Request.Builder().url(url).build();

		return request(request);
	}

	public CompletableFuture<TemplePlayer> fetchUserGainsAsync(String player, String duration) throws Exception
	{
		String playerSkillsOverviewJSON = requestUserSkillGains(player, duration);
		String playerBossingOverviewJSON = requestUserBossGains(player, duration);

		CompletableFuture<TemplePlayer> future = new CompletableFuture<>();
		future.complete(new TemplePlayer(playerSkillsOverviewJSON, playerBossingOverviewJSON, gson));
		return future;
	}

	public CompletableFuture<TempleClan> fetchClanAsync(String id, String range) throws Exception
	{
		String clanOverviewJSON = requestClanOverview(id);
		String clanAchievementsJSON = requestClanAchievements(id);
		String clanCurrentTopEhpJSON = requestClanCurrentTop("ehp", id, range);
		String clanCurrentTopEhbJSON = requestClanCurrentTop("ehb", id, range);

		CompletableFuture<TempleClan> future = new CompletableFuture<>();
		future.complete(new TempleClan(clanOverviewJSON, clanAchievementsJSON, clanCurrentTopEhpJSON, clanCurrentTopEhbJSON, gson));
		return future;
	}

	public CompletableFuture<TempleCompetition> fetchCompetitionAsync(String id) throws Exception
	{
		String competitionOverviewJSON = requestCompetitionInfo(id);

		CompletableFuture<TempleCompetition> future = new CompletableFuture<>();
		future.complete(new TempleCompetition(competitionOverviewJSON, gson));
		return future;
	}

	public CompletableFuture<TempleSync> syncClanMembersAsync(String id, String key, List<String> members) throws Exception
	{
		String syncResponseJSON;

		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("edit_group.php").build();

		RequestBody formBody = new FormBody.Builder().add("id", id).add("key", key).add("memberlist", String.valueOf(members)).build();

		Request request = new Request.Builder().url(url).post(formBody).build();

		syncResponseJSON = request(request);

		CompletableFuture<TempleSync> future = new CompletableFuture<>();
		future.complete(new TempleSync(syncResponseJSON, gson));
		return future;
	}

	public CompletableFuture<TempleSync> addClanMembersAsync(String id, String key, List<String> members) throws Exception
	{
		String syncResponseJSON;

		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("api").addPathSegment("add_group_member.php").build();

		RequestBody formBody = new FormBody.Builder().add("id", id).add("key", key).add("players", String.valueOf(members)).build();

		Request request = new Request.Builder().url(url).post(formBody).build();

		syncResponseJSON = request(request);

		CompletableFuture<TempleSync> future = new CompletableFuture<>();
		future.complete(new TempleSync(syncResponseJSON, gson));
		return future;
	}

	public void addDatapointAsync(String username, long accountHash) throws Exception
	{
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("templeosrs.com").addPathSegment("php").addPathSegment("add_datapoint.php").addQueryParameter("player", username).addQueryParameter("accountHash", Long.toString(accountHash)).build();

		Request request = new Request.Builder().url(url).build();

		request(request);
	}
}
