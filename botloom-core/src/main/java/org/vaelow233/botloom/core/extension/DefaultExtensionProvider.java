package org.vaelow233.botloom.core.extension;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.vaelow233.botloom.core.BotLoom;

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
        this.pluginManager = new DefaultPluginManager(this.extensionDirectory);
    }

    @Override
    public void load(BotLoomContext context) throws IOException {
        Files.createDirectories(extensionDirectory);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        extensions.addAll(pluginManager.getExtensions(BotLoomExtension.class));
        for (BotLoomExtension extension : extensions) {
            try {
                extension.enable(context);
            } catch (Exception | LinkageError e) {
                context.logger().error("Failed to enable extension", e);
                try {
                    extension.disable();
                } catch (Exception | LinkageError e2) {
                    context.logger().error("Failed to disable extension", e2);
                }
            }
        }
    }

    @Override
    public void unload(BotLoomContext context) {
        for (BotLoomExtension extension : extensions) {
            try {
                extension.disable();
            } catch (Exception | LinkageError e) {
                context.logger().error("Failed to disable extension", e);
            }
        }
        try {
            pluginManager.stopPlugins();
        } catch (Exception | LinkageError e) {
            context.logger().error("Failed to stop extensions", e);
        }
        try {
            pluginManager.unloadPlugins();
        } catch (Exception | LinkageError e) {
            context.logger().error("Failed to unload extensions", e);
        }
    }

    @Override
    public Set<BotLoomExtension> extensions() {
        return extensions;
    }
}
