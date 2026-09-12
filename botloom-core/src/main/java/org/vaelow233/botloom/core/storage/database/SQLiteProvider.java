package org.vaelow233.botloom.core.storage.database;

import org.vaelow233.botloom.core.config.BotLoomConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SQLiteProvider extends LocalDatabaseProvider {
    public SQLiteProvider(Path dataDirectory) {
        super(dataDirectory);
    }

    @Override
    public String buildJdbcUrl(BotLoomConfig.StorageConfig.LocalStorageConfig config) {
        Path file = dataDirectory.resolve(config.file).toAbsolutePath().normalize();
        try {
            Files.createDirectories(file.getParent());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create database directory: " + file.getParent(), e);
        }
        return "jdbc:sqlite:" + file + "?" + config.arguments;
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
