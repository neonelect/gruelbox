package com.outbox.gruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OutboxChecker extends Thread {

    @Autowired
    private TransactionOutbox outbox;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // Keep flushing work until there's nothing left to flush
                log.info("Flushing...");
                while (outbox.flush()) {}
                log.info("Flushed!");
            } catch (Exception e) {
                log.error("Error flushing transaction outbox. Pausing", e);
            }
            try {
                log.info("Waiting 1 sec...");
                // When we run out of work, pause for a 1 sec before checking again
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
