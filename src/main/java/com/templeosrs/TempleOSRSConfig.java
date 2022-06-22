package com.templeosrs;

import com.templeosrs.util.PlayerRanges;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("TempleOSRS")
public interface TempleOSRSConfig extends Config
{
	@ConfigSection(
		name = "Menu Options",
		description = "Menu Options",
		position = 0
	)
	String menuOptions = "menuOptions";
	@ConfigSection(
		name = "Rank Options",
		description = "Rank Options",
		position = 10
	)
	String rankOptions = "rankOptions";
	@ConfigSection(
		name = "Clan Options",
		description = "Clan Options",
		position = 20
	)
	String clanOptions = "clanOptions";
	@ConfigSection(
		name = "Competition Options",
		description = "Competition Options",
		position = 30
	)
	String compOptions = "compOptions";

	@ConfigItem(
		keyName = "autocomplete",
		name = "Autocomplete",
		description = "Predict names when typing a name to lookup",
		position = 1,
		section = menuOptions
	)
	default boolean autocomplete()
	{
		return true;
	}

	@ConfigItem(
		keyName = "playerLookup",
		name = "Player Lookup",
		description = "Add TempleOSRS lookup option to player's 'right-click' menu",
		position = 2,
		section = menuOptions
	)
	default boolean playerLookup()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fetchDefaults",
		name = "Fetch Defaults",
		description = "Fetch default player, clan, and competition on startup",
		position = 3,
		section = menuOptions
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
		description = "Default range for player loaded on startup",
		position = 2,
		section = rankOptions
	)
	default PlayerRanges defaultRange()
	{
		return PlayerRanges.Week;
	}

	@ConfigItem(
		keyName = "clanAchievements",
		name = "Clan Achievements",
		description = "Display group achievements when fetching group-information",
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
		description = "Display group members when fetching group-information",
		position = 2,
		section = clanOptions
	)
	default boolean clanMembers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "defaultClan",
		name = "Default Clan",
		description = "Default clan loaded on startup",
		position = 3,
		section = clanOptions
	)
	default int defaultClan()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "clanKey",
		name = "Clan Key",
		description = "Required key to edit group members using the TempleOSRS API",
		position = 4,
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
		description = "Excluded ranks from group members sync (Case Sensitive).",
		position = 5,
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
		position = 6,
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
