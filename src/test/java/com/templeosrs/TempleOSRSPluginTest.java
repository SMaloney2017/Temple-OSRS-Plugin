package com.templeosrs;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TempleOSRSPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TempleOSRSPlugin.class);
		RuneLite.main(args);
	}
}