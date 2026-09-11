package org.vaelow233.botloom.core.extension;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.command.LoomCommand;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.game.Subscription;
import org.vaelow233.botloom.core.game.event.GameEvent;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botweave.core.BotWeave;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface BotLoomContext {
    BotWeave botWeave();
    Path dataDirectory();
    StorageProvider storage();
    Logger logger();
    boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj);
    void unregisterCommand(BotLoomExtension extension, String command);
    void unregisterAll(BotLoomExtension extension);
    <T> ConfigProvider<T> registerConfig(String configName, ConfigProvider<T> configProvider) throws IOException;
    <T extends GameEvent> Subscription addListener(Class<T> clazz, Consumer<T> listener);
    void broadcast(String message);
    void unicast(BotLoomPlayer player, String message);
    Set<BotLoomPlayer> players();
    Optional<BotLoomPlayer> player(String name);
    Optional<BotLoomPlayer> player(UUID uuid);
    BotLoomOfflinePlayer offlinePlayer(String name);
    BotLoomOfflinePlayer offlinePlayer(UUID uuid);
    void runSync(Runnable runnable);
}
