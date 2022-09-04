package com.maxhitalert;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MaxHitAlertTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MaxHitAlertPlugin.class);
		RuneLite.main(args);
	}
}