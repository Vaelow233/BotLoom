package org.vaelow233.botloom.core.extension;

import org.pf4j.ExtensionPoint;

public interface BotLoomExtension extends ExtensionPoint {
    default void enable(BotLoomContext context) {

    }

    default void disable() {

    }

    String name();
    String version();
}
