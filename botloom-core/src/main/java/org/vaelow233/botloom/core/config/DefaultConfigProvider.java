package org.vaelow233.botloom.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultConfigProvider<T> implements ConfigProvider<T> {
    private final Path configFile;
    private final Class<T> configClass;
    private final Class<?> resourceOwner;
    private T config;

    public DefaultConfigProvider(Path configFile, Class<T> configClass) {
        this.configFile = configFile;
        this.configClass = configClass;
        this.resourceOwner = configClass;
    }

    public DefaultConfigProvider(
            Path file, Class<T> configClass, Class<?> resourceOwner) {
        this.configFile = file;
        this.configClass = configClass;
        this.resourceOwner = resourceOwner;
    }

    @Override
    public void load() throws IOException {
        Files.createDirectories(configFile.toAbsolutePath().getParent());
        if (Files.notExists(configFile)) {
            try (InputStream input = resourceOwner.getResourceAsStream("/" + configFile.getFileName().toString())) {
                if (input == null) {
                    throw new FileNotFoundException("Bundled " + configFile.getFileName().toString() + " was not found");
                }
                Files.copy(input, configFile);
            }
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            T loaded = mapper.readValue(reader, configClass);
            if (loaded == null) {
                throw new IOException("Config must not be null: " + configFile);
            }
            this.config = loaded;
        }
    }

    @Override
    public T config() {
        return config;
    }

    @Override
    public Class<T> configClass() {
        return configClass;
    }
}
