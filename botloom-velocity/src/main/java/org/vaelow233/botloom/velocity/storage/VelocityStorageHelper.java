package org.vaelow233.botloom.velocity.storage;

import org.bxteam.quark.LibraryManager;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botloom.core.storage.ExternalStorageHelper;
import org.vaelow233.botloom.core.storage.database.DatabaseType;

import java.nio.file.Path;

public class VelocityStorageHelper extends ExternalStorageHelper {
    private final LibraryManager libraryManager;

    public VelocityStorageHelper(BotLoomConfig.StorageConfig config, Path dataDirectory, LibraryManager libraryManager) {
        super(config, dataDirectory);
        this.libraryManager = libraryManager;
    }

    @Override
    protected void loadLibraries(DatabaseType type) {
        libraryManager.loadDependency("com.zaxxer:HikariCP:4.0.3");
        libraryManager.loadDependency("org.jdbi:jdbi3-core:3.39.1");
        libraryManager.loadDependency("org.jdbi:jdbi3-sqlobject:3.39.1");
        libraryManager.loadDependency("org.flywaydb:flyway-core:9.22.3");
        switch (type) {
            case SQLITE:
                libraryManager.loadDependency("org.xerial:sqlite-jdbc:3.53.4.0");
                break;
            case MYSQL:
                libraryManager.loadDependency("com.mysql:mysql-connector-j:26.7.0");
                libraryManager.loadDependency("org.flywaydb:flyway-mysql:9.22.3");
                break;
            case POSTGRES:
                libraryManager.loadDependency("org.postgresql:postgresql:42.7.13");
                break;
        }
    }
}
