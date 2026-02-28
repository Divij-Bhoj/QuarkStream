package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the QuarkStream MLOps service.
 * In production, this service orchestrates high-speed physics event ingestion 
 * via Apache Kafka and executes real-time AI anomaly detection.
 */
@SpringBootApplication
public class QuarkStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuarkStreamApplication.class, args);
	}

}
