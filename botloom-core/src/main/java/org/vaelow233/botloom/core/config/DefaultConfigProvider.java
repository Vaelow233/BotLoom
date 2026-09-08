package org.vaelow233.botloom.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultConfigProvider implements ConfigProvider {
    private final Path dataDirectory;
    private BotLoomConfig config;

    public DefaultConfigProvider(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public void load() throws IOException {
        Files.createDirectories(dataDirectory);
        Path configFile = dataDirectory.resolve("config.yml");
        if (Files.notExists(configFile)) {
            try (InputStream input = DefaultConfigProvider.class.getResourceAsStream("/config.yml")) {
                if (input == null) {
                    throw new FileNotFoundException("Bundled config.yml was not found");
                }
                Files.copy(input, configFile);
            }
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            this.config = mapper.readValue(reader, BotLoomConfig.class);
        }
    }

    @Override
    public BotLoomConfig config() {
        return config;
    }

    @Override
    public Path dataDirectory() {
        return dataDirectory;
    }
}
