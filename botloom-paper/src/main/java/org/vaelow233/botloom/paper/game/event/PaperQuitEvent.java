package org.vaelow233.botloom.paper.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.core.game.event.QuitEvent;
import org.vaelow233.botloom.paper.adapter.PaperPlayer;

public class PaperQuitEvent extends QuitEvent {
    public PaperQuitEvent(PlayerQuitEvent event) {
        super(new PaperPlayer(event.getPlayer()));
    }
}
