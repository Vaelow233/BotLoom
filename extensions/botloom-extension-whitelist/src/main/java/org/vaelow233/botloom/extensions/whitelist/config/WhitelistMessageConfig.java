package org.vaelow233.botloom.extensions.whitelist.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhitelistMessageConfig {
    public BotboundConfig botbound;
    public ServerboundConfig serverbound;

    public static class BotboundConfig {
        @JsonProperty("bound-by-other")
        public String boundByOther;
        @JsonProperty("reach-limit")
        public String reachLimit;
        @JsonProperty("on-cooldown")
        public String inCooldown;
        @JsonProperty("operation-successful")
        public String operationSuccessful;
        @JsonProperty("not-bound")
        public String notBound;
        @JsonProperty("unknown-code")
        public String unknownCode;
        @JsonProperty("shutting-down")
        public String shuttingDown;
    }

    public static class ServerboundConfig {
        public String unbind;
        public String unverify;
        public String error;
        @JsonProperty("shutting-down")
        public String shuttingDown;
    }
}
