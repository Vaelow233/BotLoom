package org.vaelow233.botloom.velocity.command.event;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import org.vaelow233.botloom.core.game.event.QuitEvent;
import org.vaelow233.botloom.velocity.adapter.VelocityPlayer;

public class VelocityQuitEvent extends QuitEvent {
    public VelocityQuitEvent(DisconnectEvent event) {
        super(new VelocityPlayer(event.getPlayer()));
    }
}
