package com.shipwrecks;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.Perspective;
import lombok.extern.slf4j.Slf4j;

import net.runelite.client.plugins.groundmarkers.GroundMarkerOverlay;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

class ShipwreckOverlay extends Overlay {

    private final ShipwreckPlugin plugin;
    private final ShipwreckConfig config;
    private final Client client;

    @Inject
    private ShipwreckOverlay(ShipwreckPlugin plugin, Client client, ShipwreckConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        for (GameObject obj : plugin.getShipwrecks()) {
            Polygon poly = obj.getCanvasTilePoly();
            if (poly != null) {
                OverlayUtil.renderPolygon(graphics, poly, Color.BLACK);
            }

            if (config.showHulls()) {
                Shape hull = obj.getConvexHull();
                if (hull != null) {
                    graphics.draw(hull);
                    OverlayUtil.renderPolygon(graphics, hull, Color.RED);
                }
            }

            if (config.showRange()){
                LocalPoint origin = obj.getLocalLocation();
                if (origin == null)
                {
                    continue;
                }

                int plane = client.getPlane();
                int tileSize = Perspective.LOCAL_TILE_SIZE;

                // Loop through a 15x15 square (-7..7)
                for (int dx = -7; dx <= 7; dx++) {
                    for (int dy = -7; dy <= 7; dy++) {
                        // Convert tile offset into LocalPoints
                        LocalPoint lp = new LocalPoint(
                                origin.getX() + dx * Perspective.LOCAL_TILE_SIZE,
                                origin.getY() + dy * Perspective.LOCAL_TILE_SIZE
                        );

                        // Convert local tile location to a ground polygon
                        Polygon poly3 = Perspective.getCanvasTilePoly(client, lp, plane);

                        if (poly3 != null) {
                            graphics.drawPolygon(poly3);    // outline
                            // g.fillPolygon(poly);  // uncomment for filled tiles
                        }
                    }
                }
            }
        }
        return null;
    }
}
