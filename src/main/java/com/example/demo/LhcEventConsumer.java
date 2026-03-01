package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LhcEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger("QuarkStream");

    private final AiInferenceEngine inferenceEngine;

    public LhcEventConsumer(AiInferenceEngine inferenceEngine) {
        this.inferenceEngine = inferenceEngine;
    }

    @KafkaListener(topics = "lhc-raw-events", groupId = "mlops-anomaly-detector")
    public void consume(String eventPayload) {
        inferenceEngine.analyzeEvent(eventPayload);
        
        // Log snapshot every 1000 events to show active monitoring
        long processed = inferenceEngine.getProcessedCount();
        if (processed % 1000 == 0) {
            double efficiency = (double) inferenceEngine.getAnomalyCount() / processed * 100.0;
            logger.info("📊 MLOps Status: Processed={} | Anomalies={} | Efficiency={}%", 
                processed, inferenceEngine.getAnomalyCount(), String.format("%.2f", efficiency));
        }
    }
}
