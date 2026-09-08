package org.vaelow233.botloom.core;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.bot.BotManager;
import org.vaelow233.botloom.core.bot.DefaultBotManager;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.extension.BotLoomContext;
import org.vaelow233.botloom.core.extension.DefaultExtensionProvider;
import org.vaelow233.botloom.core.extension.ExtensionProvider;
import org.vaelow233.botloom.core.storage.DefaultStorageProvider;
import org.vaelow233.botloom.core.storage.StorageProvider;

public interface BotLoom {
    Logger logger();
    ConfigProvider configProvider();

    /**
     * This method is to construct the platform-specific ConfigProvider and load it.
     */
    void setupConfigProvider();
    StorageProvider storageProvider();
    void setStorageProvider(StorageProvider provider);
    ExtensionProvider extensionProvider();
    void setExtensionProvider(ExtensionProvider provider);
    BotManager botManager();
    void setBotManager(BotManager manager);
    BotLoomContext context();
    void setContext(BotLoomContext context);
    void postEnable();
    void postDisable();

    default void enable() {
        try {
            logger().info("Loading config...");
            setupConfigProvider();
            logger().info("Loading storage...");
            StorageProvider storage = new DefaultStorageProvider(configProvider().config().storage);
            setStorageProvider(storage);
            storage.load();
            logger().info("Loading bots...");
            BotManager bot = new DefaultBotManager();
            setBotManager(bot);
            bot.load(configProvider().config().bot).whenComplete((unused, error) -> {
                if (error != null) {
                    logger().error("Failed to connect to bots", error);
                }
            });
            setContext(new BotLoomContext() {
                @Override
                public StorageProvider storage() {
                    return storageProvider();
                }
            });
            logger().info("Loading extensions...");
            ExtensionProvider extension = new DefaultExtensionProvider(configProvider().dataDirectory().resolve("extensions"));
            setExtensionProvider(extension);
            extension.load(context());
            postEnable();
        } catch (Exception e) {
            logger().error("Failed to enable plugin!", e);
            disable();
        }
    }

    default void disable() {
        if (extensionProvider() != null) {
            try {
                logger().info("Unloading extensions...");
                extensionProvider().unload();
            } catch (Exception e) {
                logger().error("Failed to unload extensions!", e);
            }
        }
        if (botManager() != null) {
            try {
                logger().info("Unloading bots...");
                botManager().unload();
            } catch (Exception e) {
                logger().error("Failed to unload bots!", e);
            }
        }
        if (storageProvider() != null) {
            try {
                logger().info("Unloading storage...");
                storageProvider().unload();
            } catch (Exception e) {
                logger().error("Failed to unload storage!", e);
            }
        }
        postDisable();
    }
}
