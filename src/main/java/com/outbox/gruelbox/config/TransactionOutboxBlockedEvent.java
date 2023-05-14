package com.outbox.gruelbox.config;

import org.springframework.context.ApplicationEvent;

public class TransactionOutboxBlockedEvent extends ApplicationEvent {

    private String id;

    public TransactionOutboxBlockedEvent(String id, Throwable cause) {
        super(cause);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
