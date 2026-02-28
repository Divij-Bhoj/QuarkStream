package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class AiInferenceEngine {

    private static final Logger logger = LoggerFactory.getLogger(AiInferenceEngine.class);
    private final Random random = new Random();

    public void analyzeEvent(String eventPayload) {
        // MOCK ONNX INFERENCE: In a real scenario, this would load the .onnx model
        // and evaluate the tensor. For the PoC, we simulate an anomaly detection score.
        double anomalyScore = random.nextDouble();
        
        if (anomalyScore > 0.95) {
            logger.warn("🚨 ANOMALY DETECTED! Score: {} | Event: {}", String.format("%.4f", anomalyScore), eventPayload);
            // TODO: Route to PostgreSQL / Secondary Alert Kafka Topic
        } else {
            logger.debug("Event normal. Score: {}", String.format("%.4f", anomalyScore));
        }
    }
}
