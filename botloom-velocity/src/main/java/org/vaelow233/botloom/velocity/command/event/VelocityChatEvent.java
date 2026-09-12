package org.vaelow233.botloom.velocity.command.event;

import com.velocitypowered.api.event.player.PlayerChatEvent;
import org.vaelow233.botloom.core.game.event.ChatEvent;
import org.vaelow233.botloom.velocity.adapter.VelocityPlayer;

public class VelocityChatEvent extends ChatEvent {
    private final PlayerChatEvent event;

    public VelocityChatEvent(PlayerChatEvent event) {
        super(new VelocityPlayer(event.getPlayer()), event.getMessage());
        this.event = event;
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Chat cancellation is not supported on Velocity");
    }
}
