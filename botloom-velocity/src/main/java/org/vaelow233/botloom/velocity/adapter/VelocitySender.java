package org.vaelow233.botloom.velocity.adapter;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.adapter.BotLoomSender;

import java.util.Optional;
import java.util.UUID;

public class VelocitySender extends BotLoomSender {
    private final CommandSource source;

    public VelocitySender(CommandSource source) {
        super(source instanceof Player ? ((Player) source).getUsername() : "CONSOLE",
                source instanceof Player ? ((Player) source).getUniqueId() : new UUID(0L, 0L));
        this.source = source;
    }

    @Override
    public void sendMessage(String message) {
        source.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return source.hasPermission(permission);
    }

    @Override
    public Optional<BotLoomPlayer> toPlayer() {
        if (source instanceof Player) {
            return Optional.of(new VelocityPlayer((Player) source));
        } else {
            return Optional.empty();
        }
    }
}
