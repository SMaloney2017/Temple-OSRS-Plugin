package com.templeosrs.util.clan;

import com.google.gson.annotations.SerializedName;

public class TempleClanOverviewInfo
{
	@SerializedName("id")
	public Integer id;

	@SerializedName("name")
	public String name;

	@SerializedName("youtube_link")
	public String youtubeLink;

	@SerializedName("twitter_link")
	public String twitterLink;

	@SerializedName("twitch_link")
	public String twitchLink;

	@SerializedName("discord_link")
	public String discordLink;

	@SerializedName("forum_link")
	public String forumLink;

	@SerializedName("total_xp")
	public Long totalXp;

	@SerializedName("average_xp")
	public Long averageXp;

	@SerializedName("total_ehp")
	public Double totalEhp;

	@SerializedName("average_ehp")
	public Double averageEhp;

	@SerializedName("total_ehb")
	public Double totalEhb;

	@SerializedName("average_ehb")
	public Double averageEhb;

	@SerializedName("clan_type")
	public String clanType;

	@SerializedName("member_count")
	public Integer memberCount;
}
