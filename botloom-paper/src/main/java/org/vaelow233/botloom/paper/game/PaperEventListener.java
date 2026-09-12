package org.vaelow233.botloom.paper.game;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.game.event.ServerStartedEvent;
import org.vaelow233.botloom.paper.game.event.PaperChatEvent;
import org.vaelow233.botloom.paper.game.event.PaperJoinEvent;
import org.vaelow233.botloom.paper.game.event.PaperLoginEvent;
import org.vaelow233.botloom.paper.game.event.PaperQuitEvent;

public class PaperEventListener implements Listener {
    private final BotLoom plugin;

    public PaperEventListener(BotLoom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.gameEventBus().fireEvent(new PaperJoinEvent(event));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.gameEventBus().fireEvent(new PaperQuitEvent(event));
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        plugin.gameEventBus().fireEvent(new PaperLoginEvent(event));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(PlayerChatEvent event) {
        plugin.gameEventBus().fireEvent(new PaperChatEvent(event));
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            plugin.gameEventBus().fireEvent(new ServerStartedEvent());
        }
    }
}
