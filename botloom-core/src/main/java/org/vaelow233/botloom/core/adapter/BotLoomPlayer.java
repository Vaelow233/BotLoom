package org.vaelow233.botloom.core.adapter;

import java.util.UUID;

public abstract class BotLoomPlayer extends BotLoomOfflinePlayer {
    protected BotLoomPlayer(String name, UUID uuid) {
        super(name, uuid);
    }

    public abstract void sendMessage(String message);
    public abstract boolean hasPermission(String permission);
    public abstract void kick(String reason);
    public abstract boolean isOnline();
}
