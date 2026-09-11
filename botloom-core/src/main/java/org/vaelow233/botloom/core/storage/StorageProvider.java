package org.vaelow233.botloom.core.storage;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

public interface StorageProvider {
    void load();
    void unload();
    HikariDataSource dataSource();
    Jdbi jdbi();
}
