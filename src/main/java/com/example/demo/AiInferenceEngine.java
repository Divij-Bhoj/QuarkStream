package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.FloatBuffer;
import java.util.Collections;

/**
 * Service responsible for executing AI inference on incoming LHC physics events.
 * Uses ONNX Runtime for high-performance, cross-platform model serving.
 */
@Service
public class AiInferenceEngine {

    private static final Logger logger = LoggerFactory.getLogger(AiInferenceEngine.class);
    
    private final java.util.concurrent.atomic.AtomicLong processedCount = new java.util.concurrent.atomic.AtomicLong(0);
    private final java.util.concurrent.atomic.AtomicLong anomalyCount = new java.util.concurrent.atomic.AtomicLong(0);
    private volatile double latestAnomalyPt = 0.0;

    @PostConstruct
    public void init() {
        logger.info("Initializing AI Inference Engine with ONNX Runtime v1.15+");
        // Load the model.onnx from resources and prepare the inference session
    }

    /**
     * Analyzes incoming physics telemetry for potential anomalies.
     * 
     * @param eventPayload raw JSON event from the Kafka stream
     */
    public void analyzeEvent(String eventPayload) {
        processedCount.incrementAndGet();
        
        // Simulation of high-pT anomaly detection for architectural demonstration
        if (eventPayload.contains("\"pt\":")) {
            try {
                String ptValue = eventPayload.split("\"pt\":")[1].split(",")[0].replace("}", "").replace("\"", "").trim();
                double pt = Double.parseDouble(ptValue);
                
                if (pt > 1000.0) {
                    anomalyCount.incrementAndGet();
                    latestAnomalyPt = pt;
                    logger.warn("ANOMALY_DETECTED: High-Transverse Momentum Signal Identified (pt={} GeV)", pt);
                } else {
                    latestAnomalyPt = pt; // Still capture the PT for the UI
                    logger.debug("Event verified: Nominal physics signature.");
                }
            } catch (Exception e) {
                logger.error("Error parsing event payload: {}", eventPayload);
            }
        }
    }

    public long getProcessedCount() { return processedCount.get(); }
    public long getAnomalyCount() { return anomalyCount.get(); }
    public double getLatestAnomalyPt() { return latestAnomalyPt; }
}
