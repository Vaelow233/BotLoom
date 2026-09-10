package org.vaelow233.botloom.core.command;

import org.vaelow233.botloom.core.adapter.BotLoomSender;
import org.vaelow233.botloom.core.extension.BotLoomExtension;

import java.util.List;

public interface RootCommandHandler {
    void onCommand(BotLoomSender sender, String[] args);
    List<String> suggest(BotLoomSender sender, String[] args);
    boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj);
    void unregisterCommand(BotLoomExtension extension, String command);
}
