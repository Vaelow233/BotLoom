package org.vaelow233.botloom.core.extension;

import org.vaelow233.botloom.core.BotLoom;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface ExtensionProvider {
    void load(BotLoomContext context) throws IOException;
    void unload();
    Set<BotLoomExtension> extensions();
}
