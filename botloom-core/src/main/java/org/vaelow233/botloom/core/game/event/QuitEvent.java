package org.vaelow233.botloom.core.game.event;

import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public abstract class QuitEvent implements GameEvent {
    private final BotLoomPlayer player;

    public QuitEvent(BotLoomPlayer player) {
        this.player = player;
    }

    public BotLoomPlayer player() {
        return player;
    }
}
