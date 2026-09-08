package org.vaelow233.botloom.core.storage.database;

import org.vaelow233.botloom.core.config.BotLoomConfig;

public class PostgreSQLProvider extends RemoteDatabaseProvider {
    @Override
    public String buildJdbcUrl(BotLoomConfig.StorageConfig.RemoteStorageConfig config) {
        return "jdbc:postgresql://" + config.host + ":" + config.port + "/" + config.database + "?" + config.arguments;
    }

    @Override
    public String driverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public DatabaseType type() {
        return DatabaseType.POSTGRES;
    }
}
