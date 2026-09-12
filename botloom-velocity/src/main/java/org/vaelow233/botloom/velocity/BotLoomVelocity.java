package org.vaelow233.botloom.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bxteam.quark.velocity.VelocityLibraryManager;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.core.config.*;
import org.vaelow233.botloom.core.exception.ExceptionHandler;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.ExtensionProvider;
import org.vaelow233.botloom.core.game.GameEventBus;
import org.vaelow233.botloom.core.game.GameHandler;
import org.vaelow233.botloom.core.game.event.ServerStartedEvent;
import org.vaelow233.botloom.core.game.event.ServerStoppingEvent;
import org.vaelow233.botloom.core.storage.ExternalStorageHelper;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botloom.velocity.command.VelocityCommand;
import org.vaelow233.botloom.velocity.command.VelocityEventListener;
import org.vaelow233.botloom.velocity.game.VelocityGameHandler;
import org.vaelow233.botloom.velocity.storage.VelocityStorageHelper;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "botloom", name = "BotLoom", version = "1.0.0", description = "BotLoom", authors = {"Vaelow233"})
public class BotLoomVelocity implements BotLoom {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final PluginManager pluginManager;
    private VelocityLibraryManager<BotLoomVelocity> libraryManager;
    private ConfigManager configManager;
    private StorageProvider storageProvider;
    private RootCommandHandler commandHandler;
    private ExtensionProvider extensionProvider;
    private BotManager botManager;
    private BotLoomContext context;
    private VelocityEventListener eventListener;
    private GameHandler gameHandler;
    private GameEventBus gameEventBus;
    private CommandMeta commandMeta;

    @Inject
    public BotLoomVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, PluginManager pluginManager) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.pluginManager = pluginManager;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        this.enable();
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        this.disable();
    }

    private boolean disabled;

    @Override
    public synchronized void disablePlugin() {
        disable();
    }

    @Override
    public synchronized void disable() {
        if (disabled) {
            return;
        }
        disabled = true;
        BotLoom.super.disable();
    }

    @Override
    public synchronized ExceptionHandler reload() {
        if (disabled || server.isShuttingDown()) {
            ExceptionHandler handler = new ExceptionHandler(logger());
            handler.record(new IllegalStateException("BotLoom is shutting down or disabled"), Level.WARN, "Cannot reload BotLoom");
            return handler;
        }
        return BotLoom.super.reload();
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public Path dataDirectory() {
        return dataDirectory;
    }

    @Override
    public ConfigManager configManager() {
        return configManager;
    }

    @Override
    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public ConfigProvider<BotLoomConfig> prepareConfigProvider() throws IOException {
        ConfigProvider<BotLoomConfig> provider = new DefaultConfigProvider<>(dataDirectory().resolve("config.yml"), BotLoomConfig.class);
        provider.load();
        if (provider.config() == null) {
            throw new IllegalArgumentException("config.yml must not be null");
        }
        return provider;
    }

    @Override
    public ConfigProvider<BotLoomMessageConfig> prepareMessageConfigProvider() throws IOException {
        ConfigProvider<BotLoomMessageConfig> provider = new DefaultConfigProvider<>(
                dataDirectory().resolve("messages.yml"), BotLoomMessageConfig.class);
        provider.load();
        if (provider.config() == null) {
            throw new IllegalArgumentException("messages.yml must not be null");
        }
        return provider;
    }

    @Override
    public StorageProvider prepareStorageProvider(BotLoomConfig.StorageConfig config) {
        ExternalStorageHelper helper = new VelocityStorageHelper(config, dataDirectory(), libraryManager);
        helper.load(logger());
        return helper.provider();
    }

    @Override
    public void setStorageProvider(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }

    @Override
    public StorageProvider storageProvider() {
        return storageProvider;
    }

    @Override
    public RootCommandHandler commandHandler() {
        return commandHandler;
    }

    @Override
    public void setCommandHandler(RootCommandHandler handler) {
        this.commandHandler = handler;
    }

    @Override
    public ExtensionProvider extensionProvider() {
        return extensionProvider;
    }

    @Override
    public void setExtensionProvider(ExtensionProvider provider) {
        this.extensionProvider = provider;
    }

    @Override
    public BotManager botManager() {
        return botManager;
    }

    @Override
    public void setBotManager(BotManager manager) {
        this.botManager = manager;
    }

    @Override
    public BotLoomContext context() {
        return context;
    }

    @Override
    public void setContext(BotLoomContext context) {
        this.context = context;
    }

    @Override
    public GameHandler gameHandler() {
        return gameHandler;
    }

    @Override
    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public GameHandler prepareGameHandler() {
        return new VelocityGameHandler(this);
    }

    @Override
    public GameEventBus gameEventBus() {
        return gameEventBus;
    }

    @Override
    public void setGameEventBus(GameEventBus gameEventBus) {
        this.gameEventBus = gameEventBus;
    }

    @Override
    public void preEnable() {
        this.libraryManager = new VelocityLibraryManager<>(this, logger, dataDirectory, pluginManager);
        libraryManager.addGoogleMavenCentralMirror();
    }

    @Override
    public void postEnable() {
        CommandManager commandManager = server.getCommandManager();
        this.commandMeta = commandManager.metaBuilder("botloom")
                .plugin(this)
                .build();
        commandManager.register(commandMeta, new VelocityCommand(this));
        this.eventListener = new VelocityEventListener(this);
        server.getEventManager().register(this, eventListener);
        gameEventBus().fireEvent(new ServerStartedEvent());
    }

    @Override
    public void preDisable() {
        if (server.isShuttingDown() && gameEventBus() != null) {
            gameEventBus().fireEvent(new ServerStoppingEvent());
        }
    }

    @Override
    public void postDisable() {
        if (commandMeta != null) {
            server.getCommandManager().unregister(commandMeta);
            commandMeta = null;
        }
        if (eventListener != null) {
            server.getEventManager().unregisterListener(this, eventListener);
            eventListener = null;
        }
    }

    @Override
    public String platform() {
        return "Velocity";
    }

    public ProxyServer server() {
        return server;
    }
}
