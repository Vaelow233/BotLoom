package org.vaelow233.botloom.velocity.command.event;

import com.velocitypowered.api.event.connection.PostLoginEvent;
import org.vaelow233.botloom.core.game.event.JoinEvent;
import org.vaelow233.botloom.velocity.adapter.VelocityPlayer;

public class VelocityJoinEvent extends JoinEvent {
    public VelocityJoinEvent(PostLoginEvent event) {
        super(new VelocityPlayer(event.getPlayer()));
    }
}
