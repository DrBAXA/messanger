package ua.bentleytek.messenger.service;

public class Event {
    private final EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

}

