package org.vaelow233.botloom.core.game.event;

import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public abstract class LoginEvent implements GameEvent {
    private final BotLoomPlayer player;

    protected LoginEvent(BotLoomPlayer player) {
        this.player = player;
    }

    public BotLoomPlayer player() {
        return player;
    }

    public abstract void disallow(String message);
}
