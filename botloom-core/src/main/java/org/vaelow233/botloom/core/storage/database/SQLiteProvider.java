package org.vaelow233.botloom.core.storage.database;

import org.vaelow233.botloom.core.config.BotLoomConfig;

public class SQLiteProvider extends LocalDatabaseProvider {
    @Override
    public String buildJdbcUrl(BotLoomConfig.StorageConfig.LocalStorageConfig config) {
        return "jdbc:sqlite:" + config.file + "?" + config.arguments;
    }

    @Override
    public String driverClassName() {
        return "org.sqlite.JDBC";
    }

    @Override
    public DatabaseType type() {
        return DatabaseType.SQLITE;
    }
}
