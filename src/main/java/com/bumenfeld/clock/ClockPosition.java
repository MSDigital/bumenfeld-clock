package com.bumenfeld.clock;

import java.util.Locale;

public enum ClockPosition {
    TOP_LEFT("left"),
    TOP_CENTER("center"),
    TOP_RIGHT("right");

    private final String value;

    ClockPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ClockPosition fromValue(String raw) {
        ClockPosition parsed = tryParse(raw);
        return parsed != null ? parsed : TOP_CENTER;
    }

    public static ClockPosition tryParse(String raw) {
        if (raw == null) {
            return null;
        }
        String normalized = raw.trim().toLowerCase(Locale.ROOT);
        for (ClockPosition position : values()) {
            if (position.value.equals(normalized)) {
                return position;
            }
        }
        return null;
    }
}
