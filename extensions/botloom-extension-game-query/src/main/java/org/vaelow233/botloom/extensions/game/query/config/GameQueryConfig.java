package org.vaelow233.botloom.extensions.game.query.config;

import java.util.List;

public class GameQueryConfig {
    public CommandConfig tps;
    public CommandConfig mspt;
    public CommandConfig cpu;
    public SourceConfig source;

    public static class CommandConfig {
        public boolean enable;
        public List<String> commands;
        public String message;
    }

    public static class SourceConfig {
        public List<String> users;
        public List<String> groups;
    }
}
