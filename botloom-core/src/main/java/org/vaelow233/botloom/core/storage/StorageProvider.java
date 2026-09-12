package org.vaelow233.botloom.core.storage;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.vaelow233.botloom.core.storage.database.DatabaseType;

public interface StorageProvider {
    void load();
    void unload();
    HikariDataSource dataSource();
    Jdbi jdbi();
    void migrate(String namespace, ClassLoader classLoader, String location);
    DatabaseType type();
}
