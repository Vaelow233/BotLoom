package org.vaelow233.botloom.paper.adapter;

import org.bukkit.OfflinePlayer;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;

public class PaperOfflinePlayer extends BotLoomOfflinePlayer {
    private final OfflinePlayer player;

    public PaperOfflinePlayer(OfflinePlayer player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    public OfflinePlayer player() {
        return player;
    }
}
