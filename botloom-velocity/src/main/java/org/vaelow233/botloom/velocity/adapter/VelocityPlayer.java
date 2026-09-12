package org.vaelow233.botloom.velocity.adapter;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public class VelocityPlayer extends BotLoomPlayer {
    private final Player player;

    public VelocityPlayer(Player player) {
        super(player.getUsername(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kick(String reason) {
        player.disconnect(LegacyComponentSerializer.legacySection().deserialize(reason));
    }

    @Override
    public boolean isOnline() {
        return player.isActive();
    }
}
