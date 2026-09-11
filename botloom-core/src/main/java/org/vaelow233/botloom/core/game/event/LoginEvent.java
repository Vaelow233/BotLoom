package org.vaelow233.botloom.core.game.event;

import java.net.InetAddress;

public abstract class LoginEvent implements GameEvent {
    private final String name;
    private final InetAddress address;

    protected LoginEvent(String name, InetAddress address) {
        this.name = name;
        this.address = address;
    }

    public String name() {
        return name;
    }

    public InetAddress address() {
        return address;
    }

    public abstract void disallow(String message);
}
