package org.vaelow233.botloom.extensions.message.forwarding.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MessageForwardingConfig {
    @JsonProperty("serverbound")
    public ServerboundConfig server;

    @JsonProperty("botbound")
    public BotboundConfig bot;

    public static class ServerboundConfig {
        public ForwardingConfig group;
        public ForwardingConfig direct;
        public static class ForwardingConfig {
            public boolean enable;
            public String content;
            public ForwardingLimitConfig limit;

            public static class ForwardingLimitConfig {
                public List<String> source;
                public int length;
                public String replacement;
            }
        }
    }

    public static class BotboundConfig {
        public boolean enable;
        public boolean format;
        public String content;
        public ForwardingLimitConfig limit;
        public List<String> destination;

        public static class ForwardingLimitConfig {
            public int length;
            public String replacement;
        }
    }
}
