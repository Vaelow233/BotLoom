package org.vaelow233.botloom.core.storage;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.vaelow233.botloom.core.config.BotLoomConfig;
import org.vaelow233.botloom.core.storage.database.*;

public class DefaultStorageProvider implements StorageProvider {
    private final BotLoomConfig.StorageConfig config;
    private final DatabaseProvider provider;
    private Jdbi jdbi;

    public DefaultStorageProvider(BotLoomConfig.StorageConfig config) {
        this.config = config;
        switch (config.type.toLowerCase()) {
            case "mysql":
                this.provider = new MySQLProvider();
                break;
            case "postgres":
            case "postgresql":
                this.provider = new PostgreSQLProvider();
                break;
            case "sqlite":
                this.provider = new SQLiteProvider();
                break;
            default:
                throw new IllegalArgumentException("Unknown storage type: " + config.type);
        }
    }

    @Override
    public void load() {
        provider.load(config);
        this.jdbi = Jdbi.create(dataSource());
        this.jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Override
    public void unload() {
        if (dataSource() != null && !dataSource().isClosed()) {
            dataSource().close();
        }
    }

    @Override
    public HikariDataSource dataSource() {
        return provider.dataSource();
    }

    public Jdbi jdbi() {
        return jdbi;
    }

    @Override
    public void migrate(String namespace, ClassLoader classLoader, String location) {
        Flyway.configure(classLoader)
                .dataSource(dataSource())
                .locations(location)
                .table("botloom_" + namespace + "_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load()
                .migrate();
    }

    public DatabaseType type() {
        return provider.type();
    }
}
