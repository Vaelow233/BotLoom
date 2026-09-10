package org.vaelow233.botloom.core.adapter;

import java.util.Optional;
import java.util.UUID;

public abstract class BotLoomSender {
    private final String name;
    private final UUID uuid;
    protected BotLoomSender(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String name() {
        return name;
    }

    public UUID uuid() {
        return uuid;
    }

    public abstract void sendMessage(String message);
    public abstract boolean hasPermission(String permission);
    public abstract Optional<BotLoomPlayer> toPlayer();
}
