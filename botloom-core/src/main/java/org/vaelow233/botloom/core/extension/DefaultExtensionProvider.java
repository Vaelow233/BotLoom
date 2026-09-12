package org.vaelow233.botloom.core.extension;

import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.slf4j.event.Level;
import org.vaelow233.botloom.core.exception.ExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DefaultExtensionProvider implements ExtensionProvider {
    private final Set<BotLoomExtension> extensions = new HashSet<>();
    private final Path extensionDirectory;
    private final PluginManager pluginManager;

    public DefaultExtensionProvider(Path extensionDirectory) {
        this.extensionDirectory = extensionDirectory;
        this.pluginManager = new JarPluginManager(this.extensionDirectory);
    }

    @Override
    public void load(BotLoomContext context, ExceptionHandler handler) throws IOException {
        Files.createDirectories(extensionDirectory);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        for (BotLoomExtension extension : pluginManager.getExtensions(BotLoomExtension.class)) {
            handler.attempt(() -> {
                extension.enable(context);
                extensions.add(extension);
            }, (e) -> {
                context.logger().error("Failed to enable extension", e);
                handler.attempt(extension::disable, Level.ERROR, "Failed to disable extension");
                context.unregisterAll(extension);
            });
        }
    }

    @Override
    public void unload(BotLoomContext context, ExceptionHandler handler) {
        boolean allDisabled = true;
        Iterator<BotLoomExtension> iterator = extensions.iterator();
        while (iterator.hasNext()) {
            BotLoomExtension extension = iterator.next();
            try {
                if (handler.attempt(extension::disable, Level.ERROR, "Failed to disable extension")) {
                    iterator.remove();
                } else {
                    allDisabled = false;
                }
            } finally {
                context.unregisterAll(extension);
            }
        }
        if (!allDisabled) {
            return;
        }
        if (handler.attempt(pluginManager::stopPlugins, Level.ERROR, "Failed to stop extensions")) {
            handler.attempt(pluginManager::unloadPlugins, Level.ERROR, "Failed to unload extensions");
        }
    }

    @Override
    public Set<BotLoomExtension> extensions() {
        return extensions;
    }
}
