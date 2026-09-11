package org.vaelow233.botloom.core.config;

import java.io.IOException;

public interface ConfigProvider<T> {
    void load() throws IOException;
    T config();
    Class<T> configClass();
}
