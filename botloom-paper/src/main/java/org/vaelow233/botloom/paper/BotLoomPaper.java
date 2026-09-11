package org.vaelow233.botloom.paper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bxteam.quark.paper.PaperLibraryManager;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.ExtensionProvider;
import org.vaelow233.botloom.core.storage.ExternalStorageHelper;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botloom.paper.command.PaperCommand;
import org.vaelow233.botloom.paper.storage.PaperStorageHelper;

import java.io.IOException;

public class BotLoomPaper extends JavaPlugin implements BotLoom {

    private PaperLibraryManager libraryManager;
    private ConfigProvider configProvider;
    private StorageProvider storageProvider;
    private RootCommandHandler commandHandler;
    private ExtensionProvider extensionProvider;
    private BotManager botManager;
    private BotLoomContext context;
    private PaperCommand paperCommand;

    @Override
    public Logger logger() {
        return getSLF4JLogger();
    }

    @Override
    public ConfigProvider configProvider() {
        return configProvider;
    }

    @Override
    public void setConfigProvider(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @Override
    public ConfigProvider prepareConfigProvider() throws IOException {
        ConfigProvider provider = new DefaultConfigProvider(getDataFolder().toPath());
        provider.load();
        if (provider.config() == null || provider.message() == null) {
            throw new IllegalArgumentException(
                    "config.yml and messages.yml must not be null"
            );
        }
        return provider;
    }

    @Override
    public StorageProvider prepareStorageProvider(BotLoomConfig.StorageConfig config) {
        ExternalStorageHelper helper = new PaperStorageHelper(config, libraryManager);
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
    public void preEnable() {
        this.libraryManager = new PaperLibraryManager(this);
        libraryManager.addGoogleMavenCentralMirror();
    }

    @Override
    public void preDisable() {

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
