package com.bumenfeld.clock;

import com.bumenfeld.clock.ClockCommand;
import com.bumenfeld.clock.ClockConfig;
import com.bumenfeld.clock.PlayerClockSettings;
import com.bumenfeld.clock.ClockTickSystem;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class Clock extends JavaPlugin {

    private final Config<ClockConfig> config;
    private ComponentType<EntityStore, PlayerClockSettings> playerSettingsComponent;

    public Clock(JavaPluginInit init) {
        super(init);
        this.config = withConfig("BumenfeldClock", ClockConfig.CODEC);
    }

    @Override
    public void setup() {
        config.save();
        playerSettingsComponent = getEntityStoreRegistry()
            .registerComponent(PlayerClockSettings.class, "BumenfeldClockSettings", PlayerClockSettings.CODEC);

        getEntityStoreRegistry().registerSystem(new ClockTickSystem(config, playerSettingsComponent));
        getCommandRegistry().registerCommand(new ClockCommand(this));
    }

    public Config<ClockConfig> getClockConfig() {
        return config;
    }

    public ComponentType<EntityStore, PlayerClockSettings> getPlayerSettingsComponent() {
        return playerSettingsComponent;
    }
}
