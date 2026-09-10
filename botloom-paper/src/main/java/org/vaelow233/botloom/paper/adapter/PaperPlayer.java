package org.vaelow233.botloom.paper.adapter;

import org.bukkit.entity.Player;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;

public class PaperPlayer extends BotLoomPlayer {
    private final Player player;

    public PaperPlayer(Player player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void kick(String reason) {
        player.kickPlayer(reason);
    }
}
