package com.bumenfeld.clock;

import com.bumenfeld.clock.ClockPosition;
import com.bumenfeld.clock.PlayerClockSettings;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.time.format.DateTimeFormatter;

public final class ClockCommand extends AbstractPlayerCommand {

    private final Clock plugin;
    private final RequiredArg<String> settingArg;
    private final RequiredArg<String> valueArg;

    public ClockCommand(Clock plugin) {
        super("clock", "Change your clock settings.");
        this.plugin = plugin;
        this.settingArg = withRequiredArg("setting", "format|position", ArgTypes.STRING);
        this.valueArg = withRequiredArg("value", "New value", ArgTypes.STRING);
    }

    @Override
    protected void execute(CommandContext commandContext, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        String setting = settingArg.get(commandContext);
        String value = valueArg.get(commandContext);

        if (store.getComponent(ref, Player.getComponentType()) == null) {
            commandContext.sender().sendMessage(Message.raw("Player entity not ready."));
            return;
        }

        PlayerClockSettings settings = store.getComponent(ref, plugin.getPlayerSettingsComponent());
        if (settings == null) {
            settings = PlayerClockSettings.fromDefaults(plugin.getClockConfig().get());
            store.putComponent(ref, plugin.getPlayerSettingsComponent(), settings);
        }

        if ("format".equalsIgnoreCase(setting)) {
            try {
                DateTimeFormatter.ofPattern(value);
            } catch (IllegalArgumentException ex) {
                commandContext.sender().sendMessage(Message.raw("Invalid time format. Example: HH:mm"));
                return;
            }
            settings.setFormat(value);
            store.putComponent(ref, plugin.getPlayerSettingsComponent(), settings);
            commandContext.sender().sendMessage(Message.raw("Clock format updated to: " + value));
            return;
        }

        if ("position".equalsIgnoreCase(setting)) {
            ClockPosition position = ClockPosition.tryParse(value);
            if (position == null) {
                commandContext.sender().sendMessage(Message.raw("Unknown position. Use left, center, or right."));
                return;
            }
            settings.setPosition(position);
            store.putComponent(ref, plugin.getPlayerSettingsComponent(), settings);
            commandContext.sender().sendMessage(Message.raw("Clock position updated to: " + position.getValue()));
            return;
        }

        commandContext.sender().sendMessage(Message.raw("Unknown setting. Use 'format' or 'position'."));
    }
}
