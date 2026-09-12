package org.vaelow233.botloom.core;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.bot.DefaultBotManager;
import org.vaelow233.botloom.core.command.LoomCommand;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.core.command.DefaultRootCommandHandler;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botloom.core.config.BotLoomMessageConfig;
import org.vaelow233.botloom.core.config.ConfigManager;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.exception.ExceptionHandler;
import org.vaelow233.botloom.core.extension.*;
import org.vaelow233.botloom.core.game.GameEventBus;
import org.vaelow233.botloom.core.game.GameHandler;
import org.vaelow233.botloom.core.game.event.GameEvent;
import org.vaelow233.botloom.core.game.event.ServerStartedEvent;
import org.vaelow233.botloom.core.game.event.ServerStoppingEvent;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botweave.core.BotWeave;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface BotLoom {
    Logger logger();
    Path dataDirectory();
    ConfigProvider<BotLoomConfig> prepareConfigProvider() throws IOException;
    ConfigProvider<BotLoomMessageConfig> prepareMessageConfigProvider() throws IOException;
    ConfigManager configManager();
    void setConfigManager(ConfigManager manager);
    StorageProvider prepareStorageProvider(BotLoomConfig.StorageConfig config);
    void setStorageProvider(StorageProvider provider);
    StorageProvider storageProvider();
    RootCommandHandler commandHandler();
    void setCommandHandler(RootCommandHandler handler);
    ExtensionProvider extensionProvider();
    void setExtensionProvider(ExtensionProvider provider);
    BotManager botManager();
    void setBotManager(BotManager manager);
    BotLoomContext context();
    void setContext(BotLoomContext context);
    GameHandler gameHandler();
    void setGameHandler(GameHandler gameHandler);
    GameHandler prepareGameHandler();
    GameEventBus gameEventBus();
    void setGameEventBus(GameEventBus gameEventBus);
    void preEnable();
    void postEnable();
    void preDisable();
    void postDisable();
    void disablePlugin();
    String platform();

    default String version() {
        return "1.0.0";
    }

    default void enable() {
        ExceptionHandler handler = new ExceptionHandler(logger());
        handler.attempt(this::preEnable, Level.ERROR, "Failed to handle pre-enable transaction!");
        if (abortOnErrors(handler)) {
            return;
        }
        logger().info("Loading config...");
        setConfigManager(new ConfigManager());
        setupConfigProvider(handler);
        if (abortOnErrors(handler)) {
            return;
        }
        logger().info("Loading storage...");
        setupStorageProvider(handler);
        if (abortOnErrors(handler)) {
            return;
        }
        logger().info("Loading commands...");
        RootCommandHandler commandHandler = new DefaultRootCommandHandler(this);
        setCommandHandler(commandHandler);
        if (abortOnErrors(handler)) {
            return;
        }
        logger().info("Loading game handlers...");
        handler.attempt(() -> {
            GameHandler gameHandler = prepareGameHandler();
            if (gameHandler == null) {
                throw new IllegalArgumentException("Game handler is null!");
            }
            setGameHandler(gameHandler);
        });
        if (abortOnErrors(handler)) {
            return;
        }
        logger().info("Loading game event bus...");
        GameEventBus eventBus = new GameEventBus(logger());
        setGameEventBus(eventBus);
        logger().info("Loading bots...");
        handler.attempt(() -> {
            BotManager bot = new DefaultBotManager();
            setBotManager(bot);
            bot.load(configManager().get("config.yml", BotLoomConfig.class).config().bot).whenComplete((unused, error) -> {
                if (error != null) {
                    logger().error("Failed to connect to bots", error);
                }
            });
        }, Level.ERROR, "Failed to create bot manager!");
        if (abortOnErrors(handler)) {
            return;
        }
        setContext(newContext());
        logger().info("Loading extensions...");
        handler.attempt(() -> {
            ExtensionProvider extension = new DefaultExtensionProvider(dataDirectory().resolve("extensions"));
            setExtensionProvider(extension);
            try {
                extension.load(context(), handler);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Level.ERROR, "Failed to load extensions!");
        handler.attempt(this::postEnable, Level.ERROR, "Failed to handle post-enable transaction!");
    }

    default ExceptionHandler reload() {
        ExceptionHandler handler = new ExceptionHandler(logger());
        ConfigManager nextManager = new ConfigManager();
        GameEventBus eventBus = new GameEventBus(logger());
        ConfigProvider<BotLoomConfig> nextConfig;
        ConfigProvider<BotLoomMessageConfig> nextMessage;
        StorageProvider nextStorage;
        GameHandler gameHandler;
        try {
            nextConfig = prepareConfigProvider();
            nextMessage = prepareMessageConfigProvider();
            nextManager.put("config.yml", nextConfig);
            nextManager.put("messages.yml", nextMessage);
            nextStorage = prepareStorageProvider(nextConfig.config().storage);
            gameHandler = prepareGameHandler();
        } catch (IOException | RuntimeException | LinkageError error) {
            handler.record(error, Level.ERROR, "Failed to prepare reload!");
            return handler;
        }
        if (extensionProvider() != null) {
            handler.attempt(() -> extensionProvider().unload(context(), handler), Level.ERROR, "Failed to unload extensions!");
        }
        if (handler.hasErrors()) {
            handler.attempt(nextStorage::unload, Level.ERROR, "Failed to close unused storage!");
            return handler;
        }
        setCommandHandler(new DefaultRootCommandHandler(this));
        if (botManager() != null) {
            handler.attempt(() -> botManager().unload(), Level.ERROR, "Failed to unload bots!");
        }
        if (handler.hasErrors()) {
            handler.attempt(nextStorage::unload, Level.ERROR, "Failed to close unused storage!");
            return handler;
        }
        if (storageProvider() != null) {
            handler.attempt(() -> storageProvider().unload(), Level.ERROR, "Failed to unload storage!");
        }
        if (handler.hasErrors()) {
            handler.attempt(nextStorage::unload, Level.ERROR, "Failed to close unused storage!");
            return handler;
        }
        setConfigManager(nextManager);
        setStorageProvider(nextStorage);
        setGameEventBus(eventBus);
        setGameHandler(gameHandler);
        handler.attempt(() -> {
            BotManager bot = new DefaultBotManager();
            setBotManager(bot);
            bot.load(nextConfig.config().bot)
                    .whenComplete((unused, error) -> {
                        if (error != null) {
                            logger().error("Failed to connect to bots", error);
                        }
                    });
        }, Level.ERROR, "Failed to initialize bots!");
        handler.attempt(() -> {
            ExtensionProvider extension = new DefaultExtensionProvider(
                    dataDirectory().resolve("extensions")
            );
            setExtensionProvider(extension);
            try {
                extension.load(context(), handler);
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }, Level.ERROR, "Failed to load extensions!");
        return handler;
    }

    default void disable() {
        ExceptionHandler handler = new ExceptionHandler(logger());
        handler.attempt(this::preDisable, Level.ERROR, "Failed to handle pre-disable transaction!");
        if (extensionProvider() != null) {
            handler.attempt(() -> extensionProvider().unload(context(), handler), Level.ERROR, "Failed to unload extensions!");
        }
        if (botManager() != null) {
            handler.attempt(() -> botManager().unload(), Level.ERROR, "Failed to unload bots!");
        }
        if (storageProvider() != null) {
            handler.attempt(() -> storageProvider().unload(), Level.ERROR, "Failed to unload storage!");
        }
        handler.attempt(this::postDisable, Level.ERROR, "Failed to handle post-disable transaction!");
    }

    default void setupConfigProvider(ExceptionHandler handler) {
        handler.attempt(() -> {
            try {
                configManager().put("config.yml", prepareConfigProvider());
                configManager().put("messages.yml", prepareMessageConfigProvider());
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }, Level.ERROR, "Failed to setup config!");
    }

    default void setupStorageProvider(ExceptionHandler handler) {
        handler.attempt(
                () -> setStorageProvider(prepareStorageProvider(
                        configManager().get("config.yml", BotLoomConfig.class).config().storage)),
                Level.ERROR,
                "Failed to setup storage!"
        );
    }

    default boolean abortOnErrors(ExceptionHandler handler) {
        if (!handler.hasErrors()) {
            return false;
        }
        disablePlugin();
        return true;
    }

    default BotLoomContext newContext() {
        return new DefaultBotLoomContext(this);
    }
}
