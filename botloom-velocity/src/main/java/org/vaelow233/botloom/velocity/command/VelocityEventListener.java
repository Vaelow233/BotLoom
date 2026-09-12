package org.vaelow233.botloom.velocity.command;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.velocity.command.event.VelocityChatEvent;
import org.vaelow233.botloom.velocity.command.event.VelocityJoinEvent;
import org.vaelow233.botloom.velocity.command.event.VelocityLoginEvent;
import org.vaelow233.botloom.velocity.command.event.VelocityQuitEvent;

public class VelocityEventListener {
    private final BotLoom plugin;

    public VelocityEventListener(BotLoom plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLogin(com.velocitypowered.api.event.connection.LoginEvent event) {
        synchronized (plugin) {
            plugin.gameEventBus().fireEvent(new VelocityLoginEvent(event));
        }
    }

    @Subscribe
    public void onJoin(PostLoginEvent event) {
        synchronized (plugin) {
            plugin.gameEventBus().fireEvent(new VelocityJoinEvent(event));
        }
    }

    @Subscribe
    public void onQuit(DisconnectEvent event) {
        synchronized (plugin) {
            plugin.gameEventBus().fireEvent(new VelocityQuitEvent(event));
        }
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        synchronized (plugin) {
            plugin.gameEventBus().fireEvent(new VelocityChatEvent(event));
        }
    }
}
