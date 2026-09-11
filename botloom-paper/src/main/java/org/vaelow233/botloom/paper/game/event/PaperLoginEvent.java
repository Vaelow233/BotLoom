package org.vaelow233.botloom.paper.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.core.game.event.LoginEvent;
import org.vaelow233.botloom.paper.adapter.PaperPlayer;

public class PaperLoginEvent extends LoginEvent {
    private final PlayerLoginEvent event;

    public PaperLoginEvent(PlayerLoginEvent event) {
        super(event.getPlayer().getName(), event.getAddress());
        this.event = event;
    }

    @Override
    public void disallow(String message) {
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
    }
}
