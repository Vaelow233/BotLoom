package org.vaelow233.botloom.core.game.event;

import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public abstract class JoinEvent implements GameEvent {
    private final BotLoomPlayer player;

    protected JoinEvent(BotLoomPlayer player) {
        this.player = player;
    }

    public BotLoomPlayer player() {
        return player;
    }
}
