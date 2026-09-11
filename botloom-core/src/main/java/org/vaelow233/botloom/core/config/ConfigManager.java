package org.vaelow233.botloom.core.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private final Map<String, ConfigProvider<?>> configProviderMap = new HashMap<>();

    public <T> ConfigProvider<T> load(String configName, ConfigProvider<T> configProvider) throws IOException {
        if (configProviderMap.containsKey(configName)) {
            throw new IllegalArgumentException(configName + " already exists");
        }
        configProvider.load();
        put(configName, configProvider);
        return configProvider;
    }

    public <T> ConfigProvider<T> put(String configName, ConfigProvider<T> configProvider) {
        if (configProviderMap.containsKey(configName)) {
            throw new IllegalArgumentException(configName + " already exists");
        }
        configProviderMap.put(configName, configProvider);
        return configProvider;
    }

    public <T> ConfigProvider<T> get(String name, Class<T> clazz) {
        ConfigProvider<?> provider = configProviderMap.get(name);
        if (provider == null) {
            throw new IllegalArgumentException("Config not registered: " + name);
        }
        if (!clazz.equals(provider.configClass())) {
            throw new IllegalArgumentException("Config type mismatch: " + name);
        }
        return (ConfigProvider<T>) provider;
    }

    public void remove(String configName) {
         configProviderMap.remove(configName);
    }

    public void clear() {
        configProviderMap.clear();
    }

    public Set<String> configNames() {
        return new HashSet<>(configProviderMap.keySet());
    }
}
