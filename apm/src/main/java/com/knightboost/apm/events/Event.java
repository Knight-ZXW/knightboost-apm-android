package com.knightboost.apm.events;

import com.google.firebase.components.Preconditions;

/**
 * Generic event object identified by its type and payload of that type.
 * Type parameters:
 * <T> – event type
 * created by Knight-ZXW on 2021/11/10
 */
public class Event<T> {
    private final Class<T> type;

    private final T payload;

    public Event(Class<T> type, T payload) {
        this.type = Preconditions.checkNotNull(type);
        this.payload = Preconditions.checkNotNull(payload);
    }

    /** The type of the event's payload. */
    public Class<T> getType() {
        return type;
    }

    /** The payload of the event. */
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("Event{type: %s, payload: %s}", type, payload);
    }
}

