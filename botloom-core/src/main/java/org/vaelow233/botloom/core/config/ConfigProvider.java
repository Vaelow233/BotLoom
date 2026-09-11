package org.vaelow233.botloom.core.config;

import java.io.IOException;
import java.nio.file.Path;

public interface ConfigProvider {
    void load() throws IOException;
    BotLoomConfig config();
    BotLoomMessageConfig message();
    Path dataDirectory();
}
