package org.vaelow233.botloom.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.velocity.adapter.VelocitySender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VelocityCommand implements SimpleCommand {
    private final BotLoom plugin;

    public VelocityCommand(BotLoom plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        synchronized (plugin) {
            plugin.commandHandler().onCommand(new VelocitySender(invocation.source()), invocation.arguments());
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        synchronized (plugin) {
            return plugin.commandHandler().suggest(new VelocitySender(invocation.source()), invocation.arguments());
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> suggest(invocation));
    }
}
