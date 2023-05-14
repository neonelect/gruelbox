package com.outbox.gruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Random;

@Slf4j
@Service
public class GruelboxService {

    @Value("${config.mambu.url}")
    private String URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GruelboxRepository gruelboxRepository;

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private OutboxChecker outboxChecker;

    @PostConstruct
    public void init() {
        this.outboxChecker.start();
    }

    @Transactional
    public void testGruelBox(String body) {
        Gruelbox gruelbox = new Gruelbox();
        gruelbox.setMessage(body);

        Long id = gruelboxRepository.save(gruelbox).getId();

        outbox.schedule(getClass()).publishCustomerCreatedEvent(id);
    }

    void publishCustomerCreatedEvent(Long id) {
        if((new Random().nextInt(9) + 1) % 2 != 0) {
            log.info("Throwing random error...");
            throw new NullPointerException();
        } else {
            String[] answer = restTemplate.getForEntity(URL, String[].class).getBody();
            log.info(id + ": " + answer[0]);
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        this.outboxChecker.interrupt();
        this.outboxChecker.join();
    }
}
