# Bumenfeld Clock

[![Gradle CI](https://github.com/msdigital/bumenfeld-clock/actions/workflows/gradle.yml/badge.svg)](https://github.com/msdigital/bumenfeld-clock/actions/workflows/gradle.yml)
[![Release](https://github.com/msdigital/bumenfeld-clock/actions/workflows/release.yml/badge.svg)](https://github.com/msdigital/bumenfeld-clock/actions/workflows/release.yml)

Bumenfeld Clock renders an in-game HUD clock for every player and lets them customize the format and screen position via `/clock`.

## 1. Description & commands

### Core features
- **In-game time HUD** - Displays the world time using the configured format (default `HH:mm`).
- **Per-player settings** - Each player can choose their own format and position.
- **Simple commands** - `/clock` updates the player settings instantly.
- **Lightweight UI** - Ships minimal `.ui` files under `Common/UI/Custom`.

### Commands
- `/clock format <pattern>` - Sets the time format (Java `DateTimeFormatter` patterns).
- `/clock position left|center|right` - Sets the HUD position.

## 2. Installation & configuration

### Dependencies
- Install the MultipleHUD dependency (`MultipleHUD-1.0.4.jar`) in `/mods/` so HUDs can be layered safely.

### Server setup
1. Build the plugin with **Java 25 (Temurin 25)**:
   ```bash
   ./gradlew clean release
   ```
   The release artifact is `build/libs/bumenfeld-clock-<version>.jar`.
2. Drop the JAR into `/mods/` of your Hytale server.
3. Start the server once so the plugin writes its config file.
4. Adjust the config as needed and restart or rejoin to apply changes.

### Configuration keys (BumenfeldClock config)
```yaml
Format: "HH:mm"          # default per-player time format
Position: "center"       # left | center | right
UpdateIntervalMs: 1000    # tick update interval (minimum 200ms)
```

## 3. UI assets
- Clock UI files live under `src/main/resources/Common/UI/Custom/` and are appended by name.
- The default layout used is `bumenfeld_clock_top_center.ui`.

## 4. Build & release
- Build the production jar with `./gradlew clean release`.
- The output artifact is `build/libs/bumenfeld-clock-<version>.jar`.
- Version metadata (ID/timestamp/commit) is injected during `processResources`, so release builds contain provenance.

## 5. Development notes
1. The plugin stores per-player settings as an entity component, seeded from the config defaults on first join.
2. UI tweaks live only in the `.ui` files; no rebuild is needed if you edit them directly in the server asset pack.
3. Keep the config defaults in sync with any UI/command changes so new players get sensible values.

## 6. License
Licensed under **MSDigital No-Resale License v1.0** (see `LICENSE`).
**Attribution:** BlackJackV8 (MSDigital) — Official repo: <https://github.com/MSDigital/bumenfeld-clock>