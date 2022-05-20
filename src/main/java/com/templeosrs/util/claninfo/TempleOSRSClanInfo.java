package com.templeosrs.util.claninfo;

import com.google.gson.annotations.SerializedName;

public class TempleOSRSClanInfo
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

	@SerializedName("member_count")
	public Integer memberCount;
}
