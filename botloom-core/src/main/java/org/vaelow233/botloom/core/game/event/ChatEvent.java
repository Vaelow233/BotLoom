package org.vaelow233.botloom.core.game.event;

import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public abstract class ChatEvent implements GameEvent {
    private final BotLoomPlayer player;
    private final String message;

    public ChatEvent(BotLoomPlayer player, String message) {
        this.player = player;
        this.message = message;
    }

    public BotLoomPlayer player() {
        return player;
    }

    public String message() {
        return message;
    }

    public abstract void cancel();
}
