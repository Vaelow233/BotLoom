package org.vaelow233.botloom.core.command.handler;

import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.adapter.BotLoomSender;
import org.vaelow233.botloom.core.config.BotLoomMessageConfig;

import static org.vaelow233.botloom.core.config.BotLoomMessageConfig.format;

public class VersionCommandHandler {
    private VersionCommandHandler() {

    }

    public static void handle(BotLoom plugin, BotLoomSender sender) {
        BotLoomMessageConfig message = plugin.configProvider().message();
        if (sender.hasPermission("botloom.admin")) {
            sender.sendMessage(format(message.prefix + message.version,
                    "platform", plugin.platform(),
                    "version", plugin.version()));
        } else {
            sender.sendMessage(format(message.noPermission));
        }
    }
}
