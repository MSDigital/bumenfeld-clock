## Overview

Bumenfeld Clock adds a lightweight **HUD clock** for every player and lets them customize **format** and **screen position**.

***

## Features

* **Always-visible clock HUD** (default: `HH:mm`)
* **Per-player settings** (each player chooses their own format & position)
* **Instant updates** via commands

***

## Commands

* `/clock format &lt;pattern&gt;` — Set the time format (Java `DateTimeFormatter` patterns)
* `/clock position left|center|right` — Set the HUD position

***

## Dependencies (required)

* [MultipleHUD](https://www.curseforge.com/hytale/mods/multiplehud)

***

## Installation

1. Install **MultipleHUD** (required).
2. Drop this mod’s `.jar` into your server’s `/mods/` folder.
3. Start the server once (config gets generated).
4. Adjust config if needed and restart / rejoin.  

***

## Configuration

Default configuration:

`{ "Format": "HH:mm", "Position": "center", "UpdateIntervalMs": 1000 }`

***

## Changelog

* **1.1.0 (2026-02-02)** — Require MultipleHUD for clock HUD updates; docs/UI refresh.
* **1.0.0 (2026-02-01)** — Initial release.