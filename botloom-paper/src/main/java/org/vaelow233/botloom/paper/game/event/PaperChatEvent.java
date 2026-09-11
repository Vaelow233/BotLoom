package org.vaelow233.botloom.paper.game.event;

import org.bukkit.event.player.PlayerChatEvent;
import org.vaelow233.botloom.core.game.event.ChatEvent;
import org.vaelow233.botloom.paper.adapter.PaperPlayer;

public class PaperChatEvent extends ChatEvent {
    private final PlayerChatEvent event;

    public PaperChatEvent(PlayerChatEvent event) {
        super(new PaperPlayer(event.getPlayer()), event.getMessage());
        this.event = event;
    }

    @Override
    public void cancel() {
        event.setCancelled(true);
    }
}
