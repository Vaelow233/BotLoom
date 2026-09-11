package org.vaelow233.botloom.core.exception;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExceptionHandler {
    private final List<Throwable> exceptions = new ArrayList<>();
    private final Logger logger;

    public ExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    public boolean attempt(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception | LinkageError e) {
            exceptions.add(e);
        }
        return false;
    }

    public void attempt(Runnable runnable, Consumer<Throwable> exceptionHandler) {
        try {
            runnable.run();
        } catch (Exception | LinkageError e) {
            exceptions.add(e);
            exceptionHandler.accept(e);
        }
    }

    public boolean attempt(Runnable runnable, Level level, String errorMessage) {
        try {
            runnable.run();
            return true;
        } catch (Exception | LinkageError e) {
            logger.atLevel(level).log(errorMessage, e);
            exceptions.add(e);
        }
        return false;
    }

    public void record(Throwable error, Level level, String message) {
        exceptions.add(error);
        logger.atLevel(level).log(message, error);
    }

    public boolean hasErrors() {
        return !exceptions.isEmpty();
    }

    public List<Throwable> exceptions() {
        return new ArrayList<>(exceptions);
    }
}
