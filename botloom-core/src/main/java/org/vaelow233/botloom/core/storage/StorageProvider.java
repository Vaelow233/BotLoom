package org.vaelow233.botloom.core.storage;

import com.zaxxer.hikari.HikariDataSource;

public interface StorageProvider {
    void load();
    void unload();
    HikariDataSource dataSource();
}
