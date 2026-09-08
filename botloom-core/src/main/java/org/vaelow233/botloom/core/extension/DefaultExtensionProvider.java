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
            extension.enable(context);
        }
    }

    @Override
    public void unload() {
        for (BotLoomExtension extension : extensions) {
            extension.disable();
        }
        pluginManager.stopPlugins();
        pluginManager.unloadPlugins();
    }

    @Override
    public Set<BotLoomExtension> extensions() {
        return extensions;
    }
}
