package com.templeosrs;

import com.templeosrs.util.CurrentTopRanges;
import com.templeosrs.util.PlayerRanges;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("TempleOSRS")
public interface TempleOSRSConfig extends Config
{
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
		keyName = "defaultPlayer",
		name = "Default Player",
		description = "Default player loaded on startup",
		position = 1,
		section = rankOptions
	)
	default String defaultPlayer()
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
	default PlayerRanges defaultRange()
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
	default boolean clanAchievements()
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
	default boolean clanMembers()
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
	default boolean clanCurrentTop()
	{
		return false;
	}

	@ConfigItem(
		keyName = "currentTopRange",
		name = "Current-top Range",
		description = "Default time-period for current-top-ranks loaded on startup",
		position = 4,
		section = clanOptions
	)
	default CurrentTopRanges currentTopRange()
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
	default int defaultClan()
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
		description = "Excluded ranks from group-members sync (Case Sensitive).",
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
	default int defaultComp()
	{
		return 0;
	}
}
