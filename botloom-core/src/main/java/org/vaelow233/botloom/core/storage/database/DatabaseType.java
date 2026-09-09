package org.vaelow233.botloom.core.storage.database;

import java.util.*;

public enum DatabaseType {
    MYSQL(Collections.singleton("mysql")),
    SQLITE(Collections.singleton("sqlite")),
    POSTGRES(new HashSet<>(Arrays.asList("postgres", "postgresql")));

    private final Set<String> aliases;

    DatabaseType(Set<String> aliases) {
        this.aliases = aliases;
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
