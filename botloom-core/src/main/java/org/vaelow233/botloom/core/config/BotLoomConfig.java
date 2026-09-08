package org.vaelow233.botloom.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BotLoomConfig {
    public int version;
    public BotConfig bot;
    public StorageConfig storage;

    public static class BotConfig {
        public List<String> types;
        @JsonProperty("qq-ob11")
        public QQOB11Config qqOb11;

        public static class QQOB11Config {
            public String scheme;
            public String host;
            public int port;
            public String token;
        }
    }

    public static class StorageConfig {
        public String type;
        public LocalStorageConfig local;
        public RemoteStorageConfig remote;

        public static class LocalStorageConfig {
            public String file;
            public String arguments;
        }

        public static class RemoteStorageConfig {
            public String host;
            public int port;
            public String username;
            public String password;
            public String database;
            public String arguments;
        }
    }
}
