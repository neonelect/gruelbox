package com.outbox.gruelbox;

import com.outbox.gruelbox.config.TransactionOutboxBlockedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventListener implements ApplicationListener<TransactionOutboxBlockedEvent> {
    @Override
    public void onApplicationEvent(TransactionOutboxBlockedEvent event) {
        System.out.println("Received spring custom event - " + event.getId());
    }
}