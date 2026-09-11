package org.vaelow233.botloom.core.extension;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.command.LoomCommand;
import org.vaelow233.botloom.core.config.ConfigProvider;
import org.vaelow233.botloom.core.storage.StorageProvider;

import java.io.IOException;

public interface BotLoomContext {
    StorageProvider storage();
    Logger logger();
    boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj);
    void unregisterCommand(BotLoomExtension extension, String command);
    void unregisterAll(BotLoomExtension extension);
    <T> void registerConfig(String configName, ConfigProvider<T> configProvider) throws IOException;
}
