package org.vaelow233.botloom.core.adapter;

import java.util.UUID;

public class BotLoomOfflinePlayer {
    private final String name;
    private final UUID uuid;
    protected BotLoomOfflinePlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String name() {
        return name;
    }

    public UUID uuid() {
        return uuid;
    }
}
