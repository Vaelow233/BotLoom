package org.vaelow233.botloom.core.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.vaelow233.botloom.core.config.BotLoomConfig;

public abstract class RemoteDatabaseProvider implements DatabaseProvider {
    private HikariDataSource dataSource;

    protected RemoteDatabaseProvider() {

    }

    protected abstract String buildJdbcUrl(BotLoomConfig.StorageConfig.RemoteStorageConfig config);
    protected abstract String driverClassName();

    public void load(BotLoomConfig.StorageConfig.RemoteStorageConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(buildJdbcUrl(config));
        hikariConfig.setDriverClassName(driverClassName());
        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void load(BotLoomConfig.StorageConfig config) {
        load(config.remote);
    }

    @Override
    public HikariDataSource dataSource() {
        return this.dataSource;
    }
}
