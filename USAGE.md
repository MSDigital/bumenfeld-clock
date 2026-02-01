# Using Bumenfeld Clock

Bumenfeld Clock shows an in-game time HUD for each player and lets them customize the format and position.

## 1. Build the plugin
- Run `./gradlew clean release` (or `./gradlew.bat clean release` on Windows) to produce `build/libs/bumenfeld-clock-<version>.jar`.
- Copy the jar plus `manifest.json` into your server `mods/<plugin-id>/` folder.

## 2. Default settings (config)
- The plugin writes a `BumenfeldClock` config file on first launch with defaults for new players.
- Update the defaults there to change the initial format (`HH:mm`) or position (`top_center`, `top_left`, `top_right`).

## 3. Player commands
- `/clock format HH:mm` updates the player's clock format.
- `/clock position top_left|top_center|top_right` updates the player's clock position.
