package org.vaelow233.botloom.core.storage.database;

import com.zaxxer.hikari.HikariDataSource;
import org.vaelow233.botloom.core.config.BotLoomConfig;

public interface DatabaseProvider {
    HikariDataSource dataSource();
    void load(BotLoomConfig.StorageConfig config);
    DatabaseType type();
}
