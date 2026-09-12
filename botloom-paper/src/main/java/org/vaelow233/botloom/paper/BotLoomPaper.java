package org.vaelow233.botloom.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bxteam.quark.paper.PaperLibraryManager;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.core.config.*;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.ExtensionProvider;
import org.vaelow233.botloom.core.game.GameEventBus;
import org.vaelow233.botloom.core.game.GameHandler;
import org.vaelow233.botloom.core.game.event.ServerStoppingEvent;
import org.vaelow233.botloom.core.storage.ExternalStorageHelper;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botloom.paper.command.PaperCommand;
import org.vaelow233.botloom.paper.game.PaperEventListener;
import org.vaelow233.botloom.paper.game.PaperGameHandler;
import org.vaelow233.botloom.paper.storage.PaperStorageHelper;

import java.io.IOException;
import java.nio.file.Path;

public class BotLoomPaper extends JavaPlugin implements BotLoom {

    private PaperLibraryManager libraryManager;
    private ConfigManager configManager;
    private StorageProvider storageProvider;
    private RootCommandHandler commandHandler;
    private ExtensionProvider extensionProvider;
    private BotManager botManager;
    private BotLoomContext context;
    private PaperCommand paperCommand;
    private GameHandler gameHandler;
    private GameEventBus gameEventBus;

    @Override
    public Logger logger() {
        return getSLF4JLogger();
    }

    @Override
    public Path dataDirectory() {
        return getDataFolder().toPath();
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
        ExternalStorageHelper helper = new PaperStorageHelper(config, dataDirectory(), libraryManager);
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
        return new PaperGameHandler(this);
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
        this.libraryManager = new PaperLibraryManager(this);
        libraryManager.addGoogleMavenCentralMirror();
    }

    @Override
    public void preDisable() {
        if (getServer().isStopping() && gameEventBus() != null) {
            gameEventBus().fireEvent(new ServerStoppingEvent());
        }
    }

    @Override
    public void postEnable() {
        paperCommand = new PaperCommand(this);
        boolean primaryNameRegistered = getServer()
                .getCommandMap()
                .register("botloom", paperCommand);
        if (!primaryNameRegistered) {
            logger().warn("/botloom is occupied; use /botloom:botloom instead");
        }
        getServer().getPluginManager().registerEvents(new PaperEventListener(this), this);
    }

    @Override
    public void postDisable() {
        if (paperCommand == null) {
            return;
        }
        CommandMap map = getServer().getCommandMap();
        map.getKnownCommands().values().removeIf(command -> command == paperCommand);
        paperCommand.unregister(map);
        paperCommand = null;
    }

    @Override
    public void disablePlugin() {
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onEnable() {
        this.enable();
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    @Override
    public String platform() {
        return Bukkit.getServer().getName();
    }
}
