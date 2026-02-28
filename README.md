# QuarkStream: Distributed AI Anomaly Detection Service

A real-time MLOps pipeline designed to demonstrate high-throughput physics data processing using enterprise-grade infrastructure. Built as part of a technical portfolio for CERN studentship applications.

## 🌌 Overview

QuarkStream is a distributed microservice architecture that consumes live LHC event telemetry (simulated), performs real-time anomaly detection using an embedded AI model, and manages the infrastructure through modern cloud-native practices.

## 🛠 Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Kafka
- **Messaging:** Apache Kafka (Event Streaming)
- **AI/ML:** ONNX Runtime (Embedded AI Inference)
- **Infrastructure:** Docker, Kubernetes (K8s)
- **CI/CD:** GitHub Actions (Automated Build & Test)

## 🏗 Architecture

1. **Ingestion:** Consumes `lhc-raw-events` from a Kafka topic.
2. **Analysis:** Passes event payloads to an `AiInferenceEngine`.
3. **Inference:** Uses an AI model to detect anomalies in physics telemetry.
4. **Alerting:** Logs detected anomalies with high precision scoring.

## 🚀 CI/CD Pipeline

The project includes a robust GitHub Actions workflow (`.github/workflows/ci.yml`) that:
- Automatically builds the project with Gradle.
- Executes unit tests for the AI inference logic.
- Ensures the stability of the distributed system on every commit.

## 🐳 Containerization & Orchestration

- **Docker:** `Dockerfile` provided for standard OCI containerization.
- **Kubernetes:** `k8s/deployment.yaml` included for resilient, scalable deployment.

## 🚧 Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose
- A running Kafka instance (port 9092)

### Running Locally

```bash
./gradlew build
./gradlew bootRun
```

---

*Part of the Divij Bhoj Portfolio Project Series.*
