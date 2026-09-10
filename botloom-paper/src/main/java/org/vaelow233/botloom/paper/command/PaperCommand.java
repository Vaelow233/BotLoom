package org.vaelow233.botloom.paper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vaelow233.botloom.core.command.RootCommandHandler;
import org.vaelow233.botloom.paper.adapter.PaperSender;

import java.util.List;

public class PaperCommand implements CommandExecutor, TabCompleter {
    private final RootCommandHandler commandHandler;

    public PaperCommand(RootCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        commandHandler.onCommand(new PaperSender(sender), args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return commandHandler.suggest(new PaperSender(sender), args);
    }
}
