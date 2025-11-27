package com.shipwrecks;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "Shipwrecks"
)
public class ShipwreckPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ShipwreckConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ShipwreckOverlay overlay;

    private static final Set<Integer> ACTIVE_SHIPWRECKS = ImmutableSet.of(
            ObjectID.SAILING_SMALL_SHIPWRECK,
            ObjectID.SAILING_FISHERMAN_SHIPWRECK,
            ObjectID.SAILING_BARRACUDA_SHIPWRECK,
            ObjectID.SAILING_LARGE_SHIPWRECK,
            ObjectID.SAILING_PIRATE_SHIPWRECK,
            ObjectID.SAILING_MERCENARY_SHIPWRECK,
            ObjectID.SAILING_FREMENNIK_SHIPWRECK,
            ObjectID.SAILING_MERCHANT_SHIPWRECK
    );

    private static final Set<Integer> STUMP_SHIPWRECKS = ImmutableSet.of(
            ObjectID.SAILING_SMALL_SHIPWRECK_STUMP,
            ObjectID.SAILING_FISHERMAN_SHIPWRECK_STUMP,
            ObjectID.SAILING_BARRACUDA_SHIPWRECK_STUMP,
            ObjectID.SAILING_LARGE_SHIPWRECK_STUMP,
            ObjectID.SAILING_PIRATE_SHIPWRECK_STUMP,
            ObjectID.SAILING_MERCENARY_SHIPWRECK_STUMP,
            ObjectID.SAILING_FREMENNIK_SHIPWRECK_STUMP,
            ObjectID.SAILING_MERCHANT_SHIPWRECK_STUMP
    );

    @Getter(AccessLevel.PACKAGE)
    private final List<GameObject> Shipwrecks = new ArrayList<>();

    @Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
        log.debug("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
        overlayManager.remove(overlay);
        this.Shipwrecks.clear();
		log.debug("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
        this.Shipwrecks.clear();
	}

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned gameObjectSpawned) {
        GameObject object = gameObjectSpawned.getGameObject();
        if (ACTIVE_SHIPWRECKS.contains(object.getId())) {
            this.Shipwrecks.add(object);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned gameObjectDespawned) {
        GameObject object = gameObjectDespawned.getGameObject();
        this.Shipwrecks.remove(object);
    }

    public void logDebug(String s) {
        log.debug(s);
    }

	@Provides
    ShipwreckConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ShipwreckConfig.class);
	}
}
