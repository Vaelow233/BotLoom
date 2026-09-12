CREATE TABLE whitelist_binds (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at TEXT NOT NULL,
    user_id TEXT NOT NULL,
    name TEXT NOT NULL
);