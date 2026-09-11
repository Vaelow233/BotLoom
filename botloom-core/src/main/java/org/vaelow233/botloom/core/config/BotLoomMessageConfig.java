package org.vaelow233.botloom.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BotLoomMessageConfig {
    public String prefix;
    public List<String> help;
    @JsonProperty("admin-help")
    public List<String> adminHelp;
    @JsonProperty("no-permission")
    public String noPermission;
    public String success;
    public String reloading;
    @JsonProperty("unknown-command")
    public String unknownCommand;
    public String version;
    public String status;
    public String reloaded;
    @JsonProperty("reload-failed")
    public String reloadFailed;

    public static String format(String message) {
        return message.replace("&", "§");
    }

    public static String format(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            if (i != messages.size() - 1) {
                sb.append(format(messages.get(i))).append("\n");
            } else {
                sb.append(format(messages.get(i)));
            }
        }
        return sb.toString();
    }

    public static String format(String message, String placeholder, String value) {
        return format(message).replace("%" + placeholder + "%", value);
    }

    public static String format(String message, String placeholder1, String value1, String placeholder2, String value2) {
        return format(message, placeholder1, value1).replace("%" + placeholder2 + "%", value2);
    }
}
