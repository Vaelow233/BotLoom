package org.vaelow233.botloom.core.command;

import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.adapter.BotLoomSender;
import org.vaelow233.botloom.core.command.handler.HelpCommandHandler;
import org.vaelow233.botloom.core.command.handler.ReloadCommandHandler;
import org.vaelow233.botloom.core.command.handler.StatusCommandHandler;
import org.vaelow233.botloom.core.command.handler.VersionCommandHandler;
import org.vaelow233.botloom.core.config.BotLoomMessageConfig;
import org.vaelow233.botloom.core.extension.BotLoomExtension;

import java.util.*;

import static org.vaelow233.botloom.core.config.BotLoomMessageConfig.*;

public class DefaultRootCommandHandler implements RootCommandHandler {
    private final Map<String, LoomCommand> commands = new HashMap<>();
    private final Map<String, BotLoomExtension> extensionMap = new HashMap<>();
    private final BotLoom plugin;
    private static final Set<String> internalCommands = new HashSet<>(Arrays.asList("help", "reload", "status", "version"));

    public DefaultRootCommandHandler(BotLoom plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(BotLoomSender sender, String[] args) {
        BotLoomMessageConfig message = plugin.configManager().get("messages.yml", BotLoomMessageConfig.class).config();
        if (args.length == 0) {
            HelpCommandHandler.handle(plugin, sender);
            return;
        }
        switch (args[0]) {
            case "help":
                HelpCommandHandler.handle(plugin, sender);
                break;
            case "reload":
                ReloadCommandHandler.handle(plugin, sender);
                break;
            case "version":
                VersionCommandHandler.handle(plugin, sender);
                break;
            case "status":
                StatusCommandHandler.handle(plugin, sender);
                break;
            default:
                if (commands.containsKey(args[0])) {
                    try {
                        commands.get(args[0]).execute(sender, args);
                    } catch (Exception | LinkageError e) {
                        BotLoomExtension owner = extensionMap.get(args[0]);
                        plugin.logger().error("Failed to execute command {} from extension {}", args[0], owner, e);
                    }
                } else {
                    sender.sendMessage(format(message.prefix + message.unknownCommand));
                }
        }
    }

    @Override
    public List<String> suggest(BotLoomSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            String prefix = args.length == 0 ? "" : args[0];
            if (sender.hasPermission("botloom.admin")) {
                for (String command : internalCommands) {
                    if (command.startsWith(prefix)) {
                        suggestions.add(command);
                    }
                }
            }
            for (String command : commands.keySet()) {
                if (command != null && command.startsWith(prefix)) {
                    suggestions.add(command);
                }
            }
        } else {
            String commandName = args[0];
            LoomCommand command = commands.get(commandName);
            if (command == null) {
                return Collections.emptyList();
            }
            try {
                List<String> candidates = command.suggest(sender, args);
                if (candidates != null) {
                    String prefix = args[args.length - 1];
                    for (String candidate : candidates) {
                        if (candidate != null && candidate.startsWith(prefix)) {
                            suggestions.add(candidate);
                        }
                    }
                }
            } catch (Exception | LinkageError error) {
                BotLoomExtension owner = extensionMap.get(commandName);
                plugin.logger().error("Failed to suggest arguments for command {} from extension {}", commandName, owner, error);
            }
        }
        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj) {
        if (extension == null) {
            plugin.logger().warn("A 'null' extension cannot register a command handler");
            return false;
        }
        if (internalCommands.contains(command)) {
            plugin.logger().warn("The command {} is an internal command that cannot be registered by extension {}", command, extension.name());
            return false;
        }
        if (commands.containsKey(command) || (extensionMap.containsKey(command) && !extensionMap.get(command).equals(extension))) {
            plugin.logger().warn("The command {} is already registered so it cannot be registered by extension {}", command, extension.name());
            return false;
        }
        commands.put(command, commandObj);
        extensionMap.put(command, extension);
        return true;
    }

    @Override
    public void unregisterCommand(BotLoomExtension extension, String command) {
        if (extension == null) {
            plugin.logger().warn("A 'null' extension cannot unregister a command handler");
            return;
        }
        if (internalCommands.contains(command)) {
            plugin.logger().warn("The command {} is an internal command that cannot be unregistered by extension {}", command, extension.name());
            return;
        }
        if (!extensionMap.containsKey(command)) {
            plugin.logger().warn("The command {} is not exists so it cannot be unregistered by extension {}", command, extension.name());
            return;
        }
        if (!extensionMap.get(command).equals(extension)) {
            plugin.logger().warn("The command {} is registered by extension {} so it cannot be unregistered by {}",
                    command, extensionMap.get(command).name(), extension.name());
            return;
        }
        commands.remove(command);
        extensionMap.remove(command);
    }

    @Override
    public void unregisterAll(BotLoomExtension extension) {
        Iterator<Map.Entry<String, BotLoomExtension>> iterator = extensionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BotLoomExtension> entry = iterator.next();
            if (entry.getValue() == extension) {
                commands.remove(entry.getKey());
                iterator.remove();
            }
        }
    }
}
