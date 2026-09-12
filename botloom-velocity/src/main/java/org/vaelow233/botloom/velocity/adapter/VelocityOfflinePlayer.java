package org.vaelow233.botloom.velocity.adapter;

import com.velocitypowered.api.util.GameProfile;
import org.vaelow233.botloom.core.adapter.BotLoomOfflinePlayer;

public class VelocityOfflinePlayer extends BotLoomOfflinePlayer {
    private final GameProfile gameProfile;
    public VelocityOfflinePlayer(GameProfile gameProfile) {
        super(gameProfile.getName(), gameProfile.getId());
        this.gameProfile = gameProfile;
    }
}
