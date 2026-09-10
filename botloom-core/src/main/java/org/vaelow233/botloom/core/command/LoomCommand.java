package org.vaelow233.botloom.core.command;

import org.vaelow233.botloom.core.adapter.BotLoomSender;

import java.util.Collections;
import java.util.List;

public interface LoomCommand {
    void execute(BotLoomSender sender, String[] args);

    default List<String> suggest(BotLoomSender sender, String[] args) {
        return Collections.emptyList();
    }
}
