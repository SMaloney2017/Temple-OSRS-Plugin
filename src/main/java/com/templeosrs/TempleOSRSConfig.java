package com.templeosrs;

import com.templeosrs.util.CurrentTopRanges;
import com.templeosrs.util.PlayerRanges;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("TempleOSRS")
public interface TempleOSRSConfig extends Config
{
	String TEMPLE_OSRS_CONFIG_GROUP = "TempleOSRS";

	@ConfigSection(
		name = "General",
		description = "General Options",
		position = 0
	)
	String generalOptions = "generalOptions";

	@ConfigSection(
		name = "Ranks",
		description = "Rank Options",
		position = 10
	)
	String rankOptions = "rankOptions";

	@ConfigSection(
		name = "Clans",
		description = "Clan Options",
		position = 20
	)
	String clanOptions = "clanOptions";

	@ConfigSection(
		name = "Competitions",
		description = "Competition Options",
		position = 30
	)
	String compOptions = "compOptions";

	@ConfigSection(
		name = "Collection Log",
		description = "Collection Log Options",
		position = 40
	)
	String clogOptions = "clogOptions";

	@ConfigItem(
		keyName = "autocomplete",
		name = "Autocomplete",
		description = "Toggle name prediction when typing a name to lookup",
		position = 1,
		section = generalOptions
	)
	default boolean autocomplete()
	{
		return true;
	}

	@ConfigItem(
		keyName = "playerLookup",
		name = "Player Lookup",
		description = "Toggle TempleOSRS lookup option in players' right-click menus",
		position = 2,
		section = generalOptions
	)
	default boolean playerLookup()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fetchDefaults",
		name = "Fetch Defaults",
		description = "Toggle whether or not to fetch defaults on startup",
		position = 3,
		section = generalOptions
	)
	default boolean fetchDefaults()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showSidebar",
		name = "Show sidebar icon",
		description = "Display TempleOSRS plugin in the sidebar.",
		position = 4,
	    section = generalOptions
	)
	default boolean showSidebar() { return true; }

	@ConfigItem(
		keyName = "defaultPlayer",
		name = "Default Player",
		description = "Default player loaded on startup",
		position = 1,
		section = rankOptions
	)
	default String getDefaultPlayer()
	{
		return "";
	}

	@ConfigItem(
		keyName = "defaultRange",
		name = "Default Range",
		description = "Default time-period for player-ranks loaded on startup",
		position = 2,
		section = rankOptions
	)
	default PlayerRanges getDefaultRange()
	{
		return PlayerRanges.Week;
	}

	@ConfigItem(
		keyName = "autoUpdate",
		name = "Auto-Update",
		description = "Toggle whether or not to automatically update the player on TempleOSRS",
		position = 3,
		section = rankOptions
	)
	default boolean autoUpdate()
	{
		return false;
	}

	@ConfigItem(
		keyName = "clanAchievements",
		name = "Clan Achievements",
		description = "Toggle whether or not to display group-achievements when fetching group-information",
		position = 1,
		section = clanOptions
	)
	default boolean displayClanAchievements()
	{
		return true;
	}

	@ConfigItem(
		keyName = "clanMembers",
		name = "Clan Members",
		description = "Toggle whether or not to display group-members when fetching group-information",
		position = 2,
		section = clanOptions
	)
	default boolean displayClanMembers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "clanCurrentTop",
		name = "Current Top Rankings",
		description = "Toggle whether or not to display weekly-top players for ehp and ehb when fetching group-information",
		position = 3,
		section = clanOptions
	)
	default boolean displayClanCurrentTop()
	{
		return true;
	}

	@ConfigItem(
		keyName = "currentTopRange",
		name = "Current Top Range",
		description = "Default time-period for current-top-ranks loaded on clan-fetch",
		position = 4,
		section = clanOptions
	)
	default CurrentTopRanges getCurrentTopRange()
	{
		return CurrentTopRanges.Week;
	}

	@ConfigItem(
		keyName = "defaultClan",
		name = "Default Clan",
		description = "Default clan loaded on startup",
		position = 5,
		section = clanOptions
	)
	default int getDefaultClan()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "clanKey",
		name = "Clan Key",
		description = "Required key to edit group-members using the TempleOSRS API",
		position = 6,
		secret = true,
		section = clanOptions
	)
	default String clanKey()
	{
		return "";
	}

	@ConfigItem(
		keyName = "ignoredRanks",
		name = "Ignored ranks",
		description = "A comma-separated list of excluded ranks from group-members sync (Case Sensitive).",
		position = 7,
		section = clanOptions
	)
	default String getIgnoredRanks()
	{
		return "";
	}

	@ConfigItem(
		keyName = "onlyAddMembers",
		name = "Only Add Members",
		description = "Toggle whether or not to only add members during clan-sync",
		position = 8,
		section = clanOptions
	)
	default boolean onlyAddMembers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "defaultComp",
		name = "Default Competition",
		description = "Default competition loaded on startup",
		position = 1,
		section = compOptions
	)
	default int getDefaultComp()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "competitionWatchlist",
		name = "Competition Watchlist",
		description = "A comma-separated list of saved competition IDs.",
		position = 2,
		section = compOptions
	)
	default String getCompetitionWatchlist()
	{
		return "";
	}

	@ConfigItem(
		keyName = "competitionWatchlist",
		name = "",
		description = ""
	)
	void setCompetitionWatchlist(String key);

	@ConfigItem(
			keyName = "clogsyncbutton",
			name = "Collection Log Update Button",
			description = "Add a button to the collection log interface to update your collection log on TempleOSRS",
			position = 1,
			section = clogOptions
	)
	default boolean clogSyncButton()
	{
		return true;
	}

	@ConfigItem(
			keyName = "autoSyncClog",
			name = "Automatically sync Collection Log",
			description = "When enabled, TempleOSRS will automatically sync your collection log when you receive a new item",
			position = 2,
			section = clogOptions
	)
	default boolean autoSyncClog()
	{
		return false;
	}

	@Range(min = 50, max = 200)
	@ConfigItem(
			keyName = "maxCachedPlayers",
			name = "Max Cached Players",
			description = "Maximum number of players to keep in the database (excluding yourself)." +
					" The more players the more MB kept on database. " +
					"Default number of players in database is 50",
			position = 3,
			section = clogOptions
	)
	default int maxCachedPlayers() { return 50; }

	@ConfigItem(
			keyName = "enableClogChatCommand",
			name = "Enable !col chat command",
			description = "When enabled, the plugin will detect any !col chat messages and replace the contents with " +
					"TempleOSRS log data, if available",
			position = 4,
			section = clogOptions
	)
	default boolean enableClogChatCommand() { return true; }

	@ConfigItem(
		keyName = "enableClogChatCommandItemNameTooltip",
		name = "Enable !col item name tooltip",
		description = "When enabled, hovering over an item icon in the !col command's message will reveal the item's name.",
		position = 5,
		section = clogOptions
	)
	default boolean enableClogChatCommandItemNameTooltip()
	{
		return true;
	}
}
