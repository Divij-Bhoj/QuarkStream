package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final AiInferenceEngine engine;

    public DashboardController(AiInferenceEngine engine) {
        this.engine = engine;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
            "processedCount", engine.getProcessedCount(),
            "anomalyCount", engine.getAnomalyCount(),
            "latestAnomaly", engine.getLatestAnomalyPt()
        );
    }
}
