package org.vaelow233.botloom.core.command;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.adapter.BotLoomSender;
import org.vaelow233.botloom.core.extension.BotLoomExtension;

import java.util.*;

public class DefaultRootCommandHandler implements RootCommandHandler {
    private final Map<String, LoomCommand> commands = new HashMap<>();
    private final Map<String, BotLoomExtension> extensionMap = new HashMap<>();
    private final Logger logger;
    private static final Set<String> internalCommands = new HashSet<>(Arrays.asList("help", "reload", "status", "version"));

    public DefaultRootCommandHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onCommand(BotLoomSender sender, String[] args) {
        if (args.length == 0) {
            // TODO: show the help message
            return;
        }
        switch (args[0]) {
            case "help":
                // TODO: show the help message
                break;
            case "reload":
                // TODO: reload the plugin
                break;
            case "version":
                // TODO: show the version of the plugin
                break;
            case "status":
                // TODO: show the status of the plugin
                break;
            default:
                if (commands.containsKey(args[0])) {
                    try {
                        commands.get(args[0]).execute(sender, args);
                    } catch (Exception | LinkageError e) {
                        BotLoomExtension owner = extensionMap.get(args[0]);
                        logger.error("Failed to execute command {} from extension {}", args[0], owner, e);
                    }
                } else {
                    // TODO: show the error message of unknown command
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
                logger.error("Failed to suggest arguments for command {} from extension {}", commandName, owner, error);
            }
        }
        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public boolean addCommand(BotLoomExtension extension, String command, LoomCommand commandObj) {
        if (extension == null) {
            logger.warn("A 'null' extension cannot register a command handler");
            return false;
        }
        if (internalCommands.contains(command)) {
            logger.warn("The command {} is an internal command that cannot be registered by extension {}", command, extension.name());
            return false;
        }
        if (commands.containsKey(command) || (extensionMap.containsKey(command) && !extensionMap.get(command).equals(extension))) {
            logger.warn("The command {} is already registered so it cannot be registered by extension {}", command, extension.name());
            return false;
        }
        commands.put(command, commandObj);
        extensionMap.put(command, extension);
        return true;
    }

    @Override
    public void unregisterCommand(BotLoomExtension extension, String command) {
        if (extension == null) {
            logger.warn("A 'null' extension cannot unregister a command handler");
            return;
        }
        if (internalCommands.contains(command)) {
            logger.warn("The command {} is an internal command that cannot be unregistered by extension {}", command, extension.name());
            return;
        }
        if (!extensionMap.containsKey(command)) {
            logger.warn("The command {} is not exists so it cannot be unregistered by extension {}", command, extension.name());
            return;
        }
        if (!extensionMap.get(command).equals(extension)) {
            logger.warn("The command {} is registered by extension {} so it cannot be unregistered by {}",
                    command, extensionMap.get(command).name(), extension.name());
            return;
        }
        commands.remove(command);
        extensionMap.remove(command);
    }
}
