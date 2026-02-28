package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LhcEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LhcEventConsumer.class);

    @KafkaListener(topics = "lhc-raw-events", groupId = "mlops-anomaly-detector")
    public void consume(String eventPayload) {
        logger.info("Received physics event: {}", eventPayload);
        // TODO: Pass to AI Inference Engine
    }
}
