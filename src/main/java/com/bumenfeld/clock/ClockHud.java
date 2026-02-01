package com.bumenfeld.clock;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class ClockHud extends CustomUIHud {

    private final ClockPosition position;
    private String currentText;

    public ClockHud(PlayerRef playerRef, ClockPosition position, String initialText) {
        super(playerRef);
        this.position = position;
        this.currentText = initialText;
    }

    @Override
    protected void build(UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append(resolveUiFile());
        uiCommandBuilder.set("#ClockLabel.TextSpans", Message.raw(currentText));
    }

    public void updateText(String text) {
        this.currentText = text;
        UICommandBuilder builder = new UICommandBuilder();
        builder.set("#ClockLabel.TextSpans", Message.raw(text));
        update(false, builder);
    }

    private String resolveUiFile() {
        return switch (position) {
            case TOP_LEFT -> "bumenfeld_clock_top_left.ui";
            case TOP_RIGHT -> "bumenfeld_clock_top_right.ui";
            case TOP_CENTER -> "bumenfeld_clock_top_center.ui";
        };
    }
}
