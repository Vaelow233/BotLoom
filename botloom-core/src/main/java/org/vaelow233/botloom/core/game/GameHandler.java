package org.vaelow233.botloom.core.game;

import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GameHandler {
    void broadcast(String message);
    void unicast(BotLoomPlayer player, String message);
    Set<BotLoomPlayer> players();
    Optional<BotLoomPlayer> player(String name);
    Optional<BotLoomPlayer> player(UUID uuid);
    BotLoomOfflinePlayer offlinePlayer(String name);
    BotLoomOfflinePlayer offlinePlayer(UUID uuid);
    void runSync(Runnable runnable);
}
