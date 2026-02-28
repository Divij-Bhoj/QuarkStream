package com.example.demo;

import ai.onnxruntime.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service responsible for executing AI inference on incoming LHC physics events.
 * Uses ONNX Runtime for high-performance, cross-platform model serving.
 */
@Service
public class AiInferenceEngine {

    private static final Logger logger = LoggerFactory.getLogger(AiInferenceEngine.class);
    
    private final AtomicLong processedCount = new AtomicLong(0);
    private final AtomicLong anomalyCount = new AtomicLong(0);
    private volatile double latestAnomalyPt = 0.0;

    private OrtEnvironment env;
    private OrtSession session;
    private final ResourceLoader resourceLoader;

    public AiInferenceEngine(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            logger.info("Initializing Authentic AI Inference Engine with ONNX Runtime...");
            env = OrtEnvironment.getEnvironment();
            
            // Load the trained model from resources
            Resource resource = resourceLoader.getResource("classpath:model.onnx");
            try (InputStream is = resource.getInputStream()) {
                byte[] modelBytes = is.readAllBytes();
                session = env.createSession(modelBytes, new OrtSession.SessionOptions());
            }
            
            logger.info("✅ ONNX Model (IsolationForest) loaded successfully.");
        } catch (Exception e) {
            logger.error("❌ Failed to initialize ONNX Runtime: {}", e.getMessage());
            // Fallback for demo purposes if model is missing during local dev
        }
    }

    @PreDestroy
    public void cleanup() throws OrtException {
        if (session != null) session.close();
        if (env != null) env.close();
    }

    /**
     * Analyzes incoming physics telemetry for potential anomalies using AI Inference.
     * 
     * @param eventPayload raw JSON event from the Kafka stream
     */
    public void analyzeEvent(String eventPayload) {
        processedCount.incrementAndGet();
        
        try {
            // Authentic MLOps: Extracting feature vector [pt, eta, phi, energy, mass]
            // In a real experiment, we would use a proper JSON parser (Jackson)
            double pt = extractDouble(eventPayload, "pt");
            double eta = extractDouble(eventPayload, "eta");
            double phi = extractDouble(eventPayload, "phi");
            double mass = extractDouble(eventPayload, "mass");
            double met = extractDouble(eventPayload, "met");

            if (session == null) {
                // Architectural demonstration fallback if model didn't load
                if (pt > 1000.0) {
                    anomalyCount.incrementAndGet();
                    latestAnomalyPt = pt;
                }
                return;
            }

            // Prepare 5-feature float tensor
            float[] features = { (float)pt, (float)eta, (float)phi, (float)mass, (float)met };
            FloatBuffer buffer = FloatBuffer.wrap(features);
            OnnxTensor tensor = OnnxTensor.createTensor(env, buffer, new long[]{1, 5});

            try (OrtSession.Result result = session.run(Collections.singletonMap("float_input", tensor))) {
                // IsolationForest in ONNX returns -1 for anomalies, 1 for normal
                // Result is often returned as a 2D array [batch][prediction]
                long[][] predictions = (long[][]) result.get(0).getValue();
                long prediction = predictions[0][0];
                
                if (prediction == -1) {
                    anomalyCount.incrementAndGet();
                    latestAnomalyPt = pt;
                    logger.warn("🚨 AI_ANOMALY: IsolationForest flagged event with pt={} GeV", pt);
                } else {
                    latestAnomalyPt = pt;
                    logger.debug("Event verified normal by AI.");
                }
            }
        } catch (Exception e) {
            logger.error("Inference Error: {}", e.getMessage());
        }
    }

    private double extractDouble(String json, String key) {
        try {
            String pattern = "\"" + key + "\":";
            if (!json.contains(pattern)) return 0.0;
            String val = json.split(pattern)[1].split("[,}]")[0].replace("\"", "").trim();
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public long getProcessedCount() { return processedCount.get(); }
    public long getAnomalyCount() { return anomalyCount.get(); }
    public double getLatestAnomalyPt() { return latestAnomalyPt; }
}
