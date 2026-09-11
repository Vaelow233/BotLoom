package org.vaelow233.botloom.core.extension;

import org.vaelow233.botloom.core.BotLoom;
import org.vaelow233.botloom.core.exception.ExceptionHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface ExtensionProvider {
    void load(BotLoomContext context, ExceptionHandler handler) throws IOException;
    void unload(BotLoomContext context, ExceptionHandler handler);
    Set<BotLoomExtension> extensions();
}
