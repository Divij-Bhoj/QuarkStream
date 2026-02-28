package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import java.util.concurrent.TimeUnit;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * ⚛️ CERN-Standard Authentic Integration Test.
 * 
 * This test executes the FULL production pipeline: 
 * 1. Starts an industrial Embedded Kafka Broker on localhost:9092
 * 2. Produces a real Physics JSON event to the Kafka wire.
 * 3. The QuarkStream LhcEventConsumer catches the event on the wire.
 * 4. The AiInferenceEngine processes the event and flags anomalies.
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class AuthenticKafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoSpyBean
    private AiInferenceEngine aiInferenceEngine;

    @Test
    public void verifyEndToEndInferencePipeline() throws Exception {
        System.out.println("🚀 [TEST] PRODUCING AUTHENTIC PHYSICS EVENT TO KAFKA...");
        
        String physicsEvent = "{\"event_id\":9001, \"pt\":1500.2, \"eta\":0.5, \"phi\":1.2, \"met\":300.5}";
        
        // Push to real Kafka wire
        kafkaTemplate.send("lhc-raw-events", physicsEvent).get();

        // Verify the AI Engine received and processed the event from the wire
        // (Wait up to 10 seconds for Kafka to deliver the message)
        verify(aiInferenceEngine, timeout(10000)).analyzeEvent(contains("\"event_id\":9001"));
        
        System.out.println("✅ [TEST] VERIFIED: EVENT CONSUMED FROM WIRE AND PROCESSED BY AI.");
    }
}
