package com.shipwrecks;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface ShipwreckConfig extends Config
{
    @ConfigItem(
            keyName = "greeting",
            name = "Welcome Greeting",
            description = "The message to show to the user when they login"
    )
    default String greeting()
    {
        return "Hello";
    }

    @ConfigItem(
            keyName = "showHulls",
            name = "Outline active shipwrecks",
            description = "Configures whether or not the active shipwreck spots are highlighted."
    )
    default boolean showHulls()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showRange",
            name = "Outline salvaging ranges",
            description = "aaa"
    )
    default boolean showRange() { return true; }
}