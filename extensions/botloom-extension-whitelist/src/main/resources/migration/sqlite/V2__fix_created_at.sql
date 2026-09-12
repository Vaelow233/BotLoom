CREATE TABLE whitelist_binds_new (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at INTEGER NOT NULL,
    user_id TEXT NOT NULL,
    name TEXT NOT NULL
);

INSERT INTO whitelist_binds_new (id, created_at, user_id, name)
SELECT id, CAST(created_at AS INTEGER), user_id, name
FROM whitelist_binds;

DROP TABLE whitelist_binds;

ALTER TABLE whitelist_binds_new RENAME TO whitelist_binds;