package org.vaelow233.botloom.velocity.game;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.game.GameHandler;
import org.vaelow233.botloom.velocity.BotLoomVelocity;
import org.vaelow233.botloom.velocity.adapter.VelocityOfflinePlayer;
import org.vaelow233.botloom.velocity.adapter.VelocityPlayer;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class VelocityGameHandler implements GameHandler {
    private final BotLoomVelocity plugin;

    public VelocityGameHandler(BotLoomVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void broadcast(String message) {
        plugin.server().getAllPlayers().forEach(player -> {
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        });
    }

    @Override
    public void unicast(BotLoomPlayer player, String message) {
        plugin.server().getPlayer(player.uuid()).ifPresent(server -> {
            server.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        });
    }

    @Override
    public Set<BotLoomPlayer> players() {
        return plugin.server().getAllPlayers().stream().map(VelocityPlayer::new).collect(Collectors.toSet());
    }

    @Override
    public Optional<BotLoomPlayer> player(String name) {
        return plugin.server().getPlayer(name).map(VelocityPlayer::new);
    }

    @Override
    public Optional<BotLoomPlayer> player(UUID uuid) {
        return plugin.server().getPlayer(uuid).map(VelocityPlayer::new);
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(String name) {
        return plugin.server().getPlayer(name)
                .map(player -> new VelocityOfflinePlayer(player.getGameProfile()))
                .orElseThrow(() -> new UnsupportedOperationException("Offline player lookup is not supported on Velocity"));
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(UUID uuid) {
        return plugin.server().getPlayer(uuid)
                .map(player -> new VelocityOfflinePlayer(player.getGameProfile()))
                .orElseThrow(() -> new UnsupportedOperationException("Offline player lookup is not supported on Velocity"));
    }

    @Override
    public void runSync(Runnable runnable) {
        runnable.run();
    }
}
