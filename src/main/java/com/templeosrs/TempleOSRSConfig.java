package com.templeosrs;

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
		name = "Clan Options",
		description = "Clan Options",
		position = 3
	)
	String clanOptions = "clanOptions";

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
		keyName = "clanAchievements",
		name = "Clan Achievements",
		description = "Display group achievements when fetching group-information",
		position = 4,
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
		position = 5,
		section = clanOptions
	)
	default boolean clanMembers()
	{
		return false;
	}

	@ConfigItem(
		keyName = "clanKey",
		name = "Clan Key",
		description = "Required key to edit group members using the TempleOSRS API",
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
		description = "Excluded ranks from group-members sync (Case-Sensitive).",
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
}
