package org.vaelow233.botloom.core.storage;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botloom.core.exception.ExceptionHandler;
import org.vaelow233.botloom.core.storage.database.*;

public abstract class ExternalStorageHelper {
    private final BotLoomConfig.StorageConfig config;
    private DefaultStorageProvider provider;

    protected abstract void loadLibraries(DatabaseType type);

    public ExternalStorageHelper(BotLoomConfig.StorageConfig config) {
        this.config = config;
    }

    public void load(Logger logger) {
        try {
            loadLibraries(DatabaseType.fromString(config.type));
            this.provider = new DefaultStorageProvider(config);
            provider.load();
        } catch (Exception | LinkageError e) {
            logger.error("Error while loading storage provider", e);
            unload();
            throw e;
        }
    }

    public void unload() {
        if (provider != null && provider.dataSource() != null && !provider.dataSource().isClosed()) {
            provider.dataSource().close();
        }
    }

    public DefaultStorageProvider provider() {
        return provider;
    }
}
