package org.vaelow233.botloom.core.storage.database;

import java.util.*;

public enum DatabaseType {
    MYSQL("mysql", Collections.singleton("mysql")),
    SQLITE("sqlite", Collections.singleton("sqlite")),
    POSTGRES("postgres", new HashSet<>(Arrays.asList("postgres", "postgresql")));

    private final String name;
    private final Set<String> aliases;

    DatabaseType(String name, Set<String> aliases) {
        this.aliases = aliases;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Set<String> aliases() {
        return aliases;
    }

    public static DatabaseType fromString(String type) {
        for (DatabaseType databaseType : DatabaseType.values()) {
            if (databaseType.aliases.contains(type.toLowerCase())) {
                return databaseType;
            }
        }
        return null;
    }
}
