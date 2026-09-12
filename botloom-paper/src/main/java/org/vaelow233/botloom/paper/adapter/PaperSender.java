package org.vaelow233.botloom.paper.adapter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vaelow233.botloom.core.adapter.BotLoomPlayer;
import org.vaelow233.botloom.core.adapter.BotLoomSender;

import java.util.Optional;
import java.util.UUID;

public class PaperSender extends BotLoomSender {
    private final CommandSender sender;

    public PaperSender(CommandSender sender) {
        super(sender.getName(), sender instanceof Player ? ((Player) sender).getUniqueId() : new UUID(0L, 0L));
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public Optional<BotLoomPlayer> toPlayer() {
        if (sender instanceof Player) {
            return Optional.of(new PaperPlayer((Player) sender));
        } else {
            return Optional.empty();
        }
    }
}
