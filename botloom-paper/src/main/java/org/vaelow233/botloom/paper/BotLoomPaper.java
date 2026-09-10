package org.vaelow233.botloom.paper;

import org.bukkit.plugin.java.JavaPlugin;
import org.bxteam.quark.paper.PaperLibraryManager;
import org.slf4j.Logger;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.config.DefaultConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.ExtensionProvider;
import org.vaelow233.botloom.core.storage.ExternalStorageHelper;
import org.vaelow233.botloom.core.storage.StorageProvider;
import org.vaelow233.botloom.paper.storage.PaperStorageHelper;

public class BotLoomPaper extends JavaPlugin implements BotLoom {

    private PaperLibraryManager libraryManager;
    private ConfigProvider configProvider;
    private StorageProvider storageProvider;
    private RootCommandHandler commandHandler;
    private ExtensionProvider extensionProvider;
    private BotManager botManager;
    private BotLoomContext context;

    @Override
    public Logger logger() {
        return getSLF4JLogger();
    }

    @Override
    public ConfigProvider configProvider() {
        return configProvider;
    }

    @Override
    public void setupConfigProvider() {
        try {
            this.configProvider = new DefaultConfigProvider(getDataFolder().toPath());
            this.configProvider.load();
        } catch (Exception e) {
            logger().error("Failed to load config", e);
        }
    }

    @Override
    public StorageProvider storageProvider() {
        return storageProvider;
    }

    @Override
    public void setupStorageProvider() {
        ExternalStorageHelper helper = new PaperStorageHelper(configProvider.config().storage, libraryManager);
        helper.load(logger());
        this.storageProvider = helper.provider();
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

    }

    @Override
    public void postDisable() {

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
}
