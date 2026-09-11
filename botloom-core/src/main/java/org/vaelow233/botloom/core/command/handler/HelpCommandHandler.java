package org.vaelow233.botloom.core.command.handler;

import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.adapter.BotLoomSender;

import static org.vaelow233.botloom.core.config.BotLoomMessageConfig.format;

public class HelpCommandHandler {
    private HelpCommandHandler() {

    }

    public static void handle(BotLoom plugin, BotLoomSender sender) {
        if (sender.hasPermission("botloom.admin")) {
            sender.sendMessage(format(plugin.configProvider().message().adminHelp));
        } else {
            sender.sendMessage(format(plugin.configProvider().message().help));
        }
    }
}
