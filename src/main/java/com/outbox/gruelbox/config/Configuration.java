package com.outbox.gruelbox.config;

import com.gruelbox.transactionoutbox.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    @Lazy
    public TransactionOutbox transactionOutbox(SpringTransactionManager springTransactionManager,
                                               SpringInstantiator springInstantiator) {
        return TransactionOutbox.builder()
                .instantiator(springInstantiator)
                .transactionManager(springTransactionManager)
                .persistor(Persistor.forDialect(Dialect.POSTGRESQL_9))
                .listener(new TransactionOutboxListener() {
                    @Override
                    public void blocked(TransactionOutboxEntry entry, Throwable cause) {
                        // Spring example
                        applicationEventPublisher.publishEvent(new TransactionOutboxBlockedEvent(entry.getId(), cause));
                    }
                })
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
