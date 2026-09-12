package org.vaelow233.botloom.velocity.command.event;

import com.velocitypowered.api.event.ResultedEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.vaelow233.botloom.core.game.event.LoginEvent;
import org.vaelow233.botloom.velocity.adapter.VelocityPlayer;

public class VelocityLoginEvent extends LoginEvent {
    private final com.velocitypowered.api.event.connection.LoginEvent event;

    public VelocityLoginEvent(com.velocitypowered.api.event.connection.LoginEvent event) {
        super(new VelocityPlayer(event.getPlayer()));
        this.event = event;
    }

    @Override
    public void disallow(String message) {
        event.setResult(ResultedEvent.ComponentResult.denied(LegacyComponentSerializer.legacySection().deserialize(message)));
    }
}
