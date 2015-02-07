package com.juankysoriano.rainbow.core.event;

public class EventDispatcher {
    protected RainbowEvent event;
    protected boolean hasEvent;

    synchronized void setEvent(final RainbowEvent e) {
        this.hasEvent = true;
        this.event = e;
    }

    synchronized RainbowEvent getEvent() {
        this.hasEvent = false;
        return event;
    }

    boolean hasEvent() {
        return hasEvent;
    }
}
