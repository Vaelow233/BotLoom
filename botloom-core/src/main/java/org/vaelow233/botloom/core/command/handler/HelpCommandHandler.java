package org.vaelow233.botloom.core.command.handler;

import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.adapter.BotLoomSender;
import org.vaelow233.botloom.core.config.BotLoomMessageConfig;

import static org.vaelow233.botloom.core.config.BotLoomMessageConfig.format;

public class HelpCommandHandler {
    private HelpCommandHandler() {

    }

    public static void handle(BotLoom plugin, BotLoomSender sender) {
        BotLoomMessageConfig message = plugin.configManager().get("messages.yml", BotLoomMessageConfig.class).config();
        if (sender.hasPermission("botloom.admin")) {
            sender.sendMessage(format(message.adminHelp));
        } else {
            sender.sendMessage(format(message.help));
        }
    }
}
