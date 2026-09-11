package org.vaelow233.botloom.paper.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.paper.adapter.PaperPlayer;

public class PaperJoinEvent extends JoinEvent {
    public PaperJoinEvent(PlayerJoinEvent event) {
        super(new PaperPlayer(event.getPlayer()));
    }
}
