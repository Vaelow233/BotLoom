package org.vaelow233.botloom.extensions.game.notifications.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameNotificationsConfig {
    @JsonProperty("server-started")
    public NotificationConfig serverStarted;
    @JsonProperty("server-stopping")
    public NotificationConfig serverStopping;
    @JsonProperty("player-join")
    public NotificationConfig playerJoin;
    @JsonProperty("player-quit")
    public NotificationConfig playerQuit;
    public DestinationConfig destination;

    public static class NotificationConfig {
        public boolean enable;
        public String message;
    }

    public static class DestinationConfig {
        public List<String> users;
        public List<String> groups;
    }
}
