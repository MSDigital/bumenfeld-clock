package com.bumenfeld.clock;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.hypixel.hytale.component.ComponentType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ClockTickSystem extends EntityTickingSystem<EntityStore> {

    private static final String HUD_KEY = "BumenfeldClock";

    private final Query<EntityStore> query;
    private final Config<ClockConfig> config;
    private final ComponentType<EntityStore, PlayerClockSettings> settingsComponent;

    public ClockTickSystem(Config<ClockConfig> config, ComponentType<EntityStore, PlayerClockSettings> settingsComponent) {
        this.query = Query.and(Player.getComponentType(), PlayerRef.getComponentType());
        this.config = config;
        this.settingsComponent = settingsComponent;
    }

    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }

    @Override
    public void tick(float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store,
                     CommandBuffer<EntityStore> commandBuffer) {
        WorldTimeResource timeResource = store.getResource(WorldTimeResource.getResourceType());
        if (timeResource == null) {
            return;
        }

        LocalDateTime gameDateTime = timeResource.getGameDateTime();
        if (gameDateTime == null) {
            return;
        }

        Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        Player player = holder.getComponent(Player.getComponentType());
        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        if (player.isWaitingForClientReady()) {
            return;
        }

        HudManager hudManager = player.getHudManager();
        if (hudManager == null) {
            return;
        }

        PlayerClockSettings settings = holder.getComponent(settingsComponent);
        if (settings == null) {
            settings = PlayerClockSettings.fromDefaults(config.get());
            holder.putComponent(settingsComponent, settings);
        }

        String formatted = formatTime(gameDateTime, settings);
        if (settings.getLastRenderedPosition() != settings.getPosition()
            || !settings.getLastRenderedFormat().equals(settings.getFormat())) {
            ClockHud hud = new ClockHud(playerRef, settings.getPosition(), formatted);
            MultipleHUD multipleHUD = MultipleHUD.getInstance();
            if (multipleHUD != null) {
                multipleHUD.setCustomHud(player, playerRef, HUD_KEY, hud);
            } else {
                hudManager.setCustomHud(playerRef, hud);
            }
            settings.setLastRenderedPosition(settings.getPosition());
            settings.setLastRenderedFormat(settings.getFormat());
            settings.setLastRenderedText(formatted);
            return;
        }

        if (!formatted.equals(settings.getLastRenderedText())) {
            ClockHud hud = new ClockHud(playerRef, settings.getPosition(), formatted);
            MultipleHUD multipleHUD = MultipleHUD.getInstance();
            if (multipleHUD != null) {
                multipleHUD.setCustomHud(player, playerRef, HUD_KEY, hud);
            } else {
                hudManager.setCustomHud(playerRef, hud);
            }
            settings.setLastRenderedText(formatted);
        }
    }

    private String formatTime(LocalDateTime gameDateTime, PlayerClockSettings settings) {
        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(settings.getFormat());
        } catch (IllegalArgumentException ex) {
            formatter = DateTimeFormatter.ofPattern(config.get().getDefaultFormat());
        }
        return formatter.format(gameDateTime);
    }

}
