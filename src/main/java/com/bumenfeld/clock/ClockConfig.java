package com.bumenfeld.clock;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public final class ClockConfig {

    public static final BuilderCodec<ClockConfig> CODEC =
        BuilderCodec.builder(ClockConfig.class, ClockConfig::new)
            .append(new KeyedCodec<>("Format", Codec.STRING),
                (config, value) -> config.defaultFormat = value,
                config -> config.defaultFormat)
            .add()
            .append(new KeyedCodec<>("Position", Codec.STRING),
                (config, value) -> config.defaultPosition = value,
                config -> config.defaultPosition)
            .add()
            .append(new KeyedCodec<>("UpdateIntervalMs", Codec.INTEGER),
                (config, value) -> config.updateIntervalMs = value,
                config -> config.updateIntervalMs)
            .add()
            .build();

    private String defaultFormat;
    private String defaultPosition;
    private int updateIntervalMs;

    public ClockConfig() {
        this.defaultFormat = "HH:mm";
        this.defaultPosition = ClockPosition.TOP_CENTER.getValue();
        this.updateIntervalMs = 1000;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }

    public ClockPosition getDefaultPosition() {
        return ClockPosition.fromValue(defaultPosition);
    }

    public int getUpdateIntervalMs() {
        return updateIntervalMs;
    }
}
