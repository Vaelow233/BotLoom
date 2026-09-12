package org.vaelow233.botloom.core.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.vaelow233.botloom.core.config.BotLoomConfig;

import java.nio.file.Path;

public abstract class LocalDatabaseProvider implements DatabaseProvider {
    private HikariDataSource dataSource;
    protected Path dataDirectory;

    protected LocalDatabaseProvider(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    protected abstract String buildJdbcUrl(BotLoomConfig.StorageConfig.LocalStorageConfig config);
    protected abstract String driverClassName();

    protected void load(BotLoomConfig.StorageConfig.LocalStorageConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(buildJdbcUrl(config));
        hikariConfig.setDriverClassName(driverClassName());
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void load(BotLoomConfig.StorageConfig config) {
        load(config.local);
    }

    @Override
    public HikariDataSource dataSource() {
        return this.dataSource;
    }
}
