package org.vaelow233.botloom.paper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.vaelow233.botloom.paper.BotLoomPaper;
import org.vaelow233.botloom.paper.adapter.PaperSender;

import java.util.List;

public class PaperCommand extends Command implements PluginIdentifiableCommand {
    private final BotLoomPaper plugin;

    public PaperCommand(BotLoomPaper plugin) {
        super("botloom");
        this.plugin = plugin;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        plugin.commandHandler().onCommand(new PaperSender(sender), args);
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return plugin.commandHandler().suggest(new PaperSender(sender), args);
    }
}
