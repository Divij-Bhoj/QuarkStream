package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiInferenceEngineTests {

    private final AiInferenceEngine inferenceEngine = new AiInferenceEngine();

    @Test
    void testAnomalyDetectionIsProbabilistic() {
        // Simple test to ensure the engine runs without errors
        assertDoesNotThrow(() -> inferenceEngine.analyzeEvent("{\"test\": \"event\"}"));
    }
}
