package com.maxhitalert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("maxhitalert")
public interface MaxHitAlertConfig extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Discord Webhook",
		description = "The discord webhook url to receive alerts whenever you get a max hit."
	)
	default String greeting()
	{
		return "null";
	}
}
