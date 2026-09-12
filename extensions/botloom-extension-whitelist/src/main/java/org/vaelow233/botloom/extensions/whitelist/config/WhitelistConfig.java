package org.vaelow233.botloom.extensions.whitelist.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WhitelistConfig {
    public boolean enable;
    @JsonProperty("need-bind-to-login")
    public boolean needBindToLogin;
    @JsonProperty("max-bind-count")
    public int maxBindCount;
    public String method;
    public CodeConfig code;
    public BypassConfig bypass;
    public CooldownConfig cooldown;
    public PrefixesConfig prefixes;
    public SourcesConfig sources;

    public static class CodeConfig {
        public int length;
        public String characters;
        @JsonProperty("expires-at")
        public int expiresAt;
    }

    public static class BypassConfig {
        public boolean enable;
        public String permission;
        public List<String> names;
    }

    public static class CooldownConfig {
        public int bind;
        public int unbind;
    }

    public static class PrefixesConfig {
        public List<String> bind;
        public List<String> unbind;
        public List<String> verify;
    }

    public static class SourcesConfig {
        public List<String> users;
        public List<String> groups;
    }
}
