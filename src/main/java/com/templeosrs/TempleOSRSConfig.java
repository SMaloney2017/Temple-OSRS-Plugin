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

	@ConfigSection(
		name = "Clan Options",
		description = "Clan Options",
		position = 3
	)
	String clanOptions = "clanOptions";

	@ConfigItem(
		keyName = "clanKey",
		name = "Clan Key",
		description = "Required key to edit group members using the TempleOSRS API",
		position = 4,
		section = clanOptions
	)
	default String clanKey()
	{
		return "";
	}
}
