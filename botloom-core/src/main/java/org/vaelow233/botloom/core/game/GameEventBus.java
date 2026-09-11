package org.vaelow233.botloom.core.game;

import org.slf4j.Logger;
import org.vaelow233.botloom.core.game.event.GameEvent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class GameEventBus {
    private final CopyOnWriteArrayList<Consumer<GameEvent>> listeners = new CopyOnWriteArrayList<>();
    private final Logger logger;

    public GameEventBus(Logger logger) {
        this.logger = logger;
    }

    public <T extends GameEvent> Subscription addListener(Class<T> type, Consumer<T> listener) {
        AtomicBoolean active = new AtomicBoolean(true);
        Consumer<GameEvent> wrapper = event -> {
            if (active.get() && type.isInstance(event)) {
                listener.accept(type.cast(event));
            }
        };
        listeners.add(wrapper);
        return () -> {
            active.set(false);
            listeners.remove(wrapper);
        };
    }

    public void fireEvent(GameEvent event) {
        for (Consumer<GameEvent> listener : listeners) {
            try {
                listener.accept(event);
            } catch (Exception | LinkageError error) {
                logger.error("Failed to handle game event", error);
            }
        }
    }
}
