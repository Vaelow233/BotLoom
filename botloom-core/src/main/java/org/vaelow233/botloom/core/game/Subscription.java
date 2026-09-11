package org.vaelow233.botloom.core.game;

public interface Subscription extends AutoCloseable {
    @Override
    void close();
}