package org.vaelow233.botloom.core.storage.database;

import org.vaelow233.botloom.core.config.BotLoomConfig;

public class MySQLProvider extends RemoteDatabaseProvider {
    @Override
    public String buildJdbcUrl(BotLoomConfig.StorageConfig.RemoteStorageConfig config) {
        return "jdbc:mysql://" + config.host + ":" + config.port + "/" + config.database + "?" + config.arguments;
    }

    @Override
    public String driverClassName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public DatabaseType type() {
        return DatabaseType.MYSQL;
    }
}
