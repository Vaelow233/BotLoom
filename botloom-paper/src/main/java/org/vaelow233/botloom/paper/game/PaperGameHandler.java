package org.vaelow233.botloom.paper.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.game.GameHandler;
import org.vaelow233.botloom.paper.BotLoomPaper;
import org.vaelow233.botloom.paper.adapter.PaperOfflinePlayer;
import org.vaelow233.botloom.paper.adapter.PaperPlayer;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperGameHandler implements GameHandler {
    private final BotLoomPaper plugin;

    public PaperGameHandler(BotLoomPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    @Override
    public void unicast(BotLoomPlayer player, String message) {
        player.sendMessage(message);
    }

    @Override
    public Set<BotLoomPlayer> players() {
        return Bukkit.getOnlinePlayers().stream().map(PaperPlayer::new).collect(Collectors.toSet());
    }

    @Override
    public Optional<BotLoomPlayer> player(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player == null) {
            return Optional.empty();
        } else {
            return Optional.of(new PaperPlayer(player));
        }
    }

    @Override
    public Optional<BotLoomPlayer> player(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Optional.empty();
        } else {
            return Optional.of(new PaperPlayer(player));
        }
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(String name) {
        return new PaperOfflinePlayer(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(UUID uuid) {
        return new PaperOfflinePlayer(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void runSync(Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }
}
