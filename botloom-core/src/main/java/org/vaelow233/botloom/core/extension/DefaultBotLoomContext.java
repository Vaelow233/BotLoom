package org.vaelow233.botloom.core.extension;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.BotLoom;
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
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class DefaultBotLoomContext implements BotLoomContext {
    private final BotLoom plugin;
    
    public DefaultBotLoomContext(BotLoom plugin) {
        this.plugin = plugin;
    }

    @Override
    public BotWeave botWeave() {
        return plugin.botManager().botWeave();
    }

    @Override
    public Path dataDirectory() {
        return plugin.dataDirectory();
    }

    @Override
    public StorageProvider storage() {
        return plugin.storageProvider();
    }

    @Override
    public Logger logger() {
        return plugin.logger();
    }

    @Override
    public boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj) {
        return plugin.commandHandler().addCommand(extension, command, commandObj);
    }

    @Override
    public void unregisterCommand(BotLoomExtension extension, String command) {
        plugin.commandHandler().unregisterCommand(extension, command);
    }

    @Override
    public void unregisterAll(BotLoomExtension extension) {
        plugin.commandHandler().unregisterAll(extension);
    }

    @Override
    public <T> ConfigProvider<T> registerConfig(String configName, ConfigProvider<T> configProvider) throws IOException {
        return plugin.configManager().load(configName, configProvider);
    }

    @Override
    public <T extends GameEvent> Subscription addListener(Class<T> clazz, Consumer<T> listener) {
        return plugin.gameEventBus().addListener(clazz, listener);
    }

    @Override
    public void broadcast(String message) {
        plugin.gameHandler().broadcast(message);
    }

    @Override
    public void unicast(BotLoomPlayer player, String message) {
        plugin.gameHandler().unicast(player, message);
    }

    @Override
    public Set<BotLoomPlayer> players() {
        return plugin.gameHandler().players();
    }

    @Override
    public Optional<BotLoomPlayer> player(String name) {
        return plugin.gameHandler().player(name);
    }

    @Override
    public Optional<BotLoomPlayer> player(UUID uuid) {
        return plugin.gameHandler().player(uuid);
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(String name) {
        return plugin.gameHandler().offlinePlayer(name);
    }

    @Override
    public BotLoomOfflinePlayer offlinePlayer(UUID uuid) {
        return plugin.gameHandler().offlinePlayer(uuid);
    }

    @Override
    public void runSync(Runnable runnable) {
        plugin.gameHandler().runSync(runnable);
    }

    @Override
    public CompletionStage<Void> botsReady() {
        return plugin.botManager().ready();
    }
}
