package com.bumenfeld.clock;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public final class PlayerClockSettings implements Component<EntityStore> {

    public static final BuilderCodec<PlayerClockSettings> CODEC =
        BuilderCodec.builder(PlayerClockSettings.class, PlayerClockSettings::new)
            .append(new KeyedCodec<>("Format", Codec.STRING),
                (data, value) -> data.format = value,
                data -> data.format)
            .add()
            .append(new KeyedCodec<>("Position", Codec.STRING),
                (data, value) -> data.position = value,
                data -> data.position)
            .add()
            .build();

    private String format;
    private String position;
    private String lastRenderedText;
    private String lastRenderedFormat;
    private ClockPosition lastRenderedPosition;

    public PlayerClockSettings() {
        this.format = "HH:mm";
        this.position = ClockPosition.TOP_CENTER.getValue();
        this.lastRenderedText = "";
        this.lastRenderedFormat = this.format;
        this.lastRenderedPosition = ClockPosition.TOP_CENTER;
    }

    private PlayerClockSettings(String format, String position) {
        this.format = format;
        this.position = position;
        this.lastRenderedText = "";
        this.lastRenderedFormat = format;
        this.lastRenderedPosition = ClockPosition.fromValue(position);
    }

    public static PlayerClockSettings fromDefaults(ClockConfig config) {
        return new PlayerClockSettings(config.getDefaultFormat(), config.getDefaultPosition().getValue());
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ClockPosition getPosition() {
        return ClockPosition.fromValue(position);
    }

    public void setPosition(ClockPosition position) {
        this.position = position.getValue();
    }

    public String getLastRenderedText() {
        return lastRenderedText;
    }

    public void setLastRenderedText(String lastRenderedText) {
        this.lastRenderedText = lastRenderedText;
    }

    public String getLastRenderedFormat() {
        return lastRenderedFormat;
    }

    public void setLastRenderedFormat(String lastRenderedFormat) {
        this.lastRenderedFormat = lastRenderedFormat;
    }

    public ClockPosition getLastRenderedPosition() {
        return lastRenderedPosition;
    }

    public void setLastRenderedPosition(ClockPosition lastRenderedPosition) {
        this.lastRenderedPosition = lastRenderedPosition;
    }

    @Override
    public PlayerClockSettings clone() {
        PlayerClockSettings cloned = new PlayerClockSettings(format, position);
        cloned.lastRenderedText = lastRenderedText;
        cloned.lastRenderedFormat = lastRenderedFormat;
        cloned.lastRenderedPosition = lastRenderedPosition;
        return cloned;
    }
}
