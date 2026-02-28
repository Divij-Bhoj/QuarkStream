import json
import time
import numpy as np
from kafka import KafkaProducer

def inject_physics_event(features, anomaly=False):
    producer = KafkaProducer(
        bootstrap_servers=['localhost:9092'],
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )
    
    event = {
        "event_id": int(time.time()),
        "pt": features[0],
        "eta": features[1],
        "phi": features[2],
        "mass": features[3],
        "met": features[4]
    }
    
    print(f"🚀 Injecting {'ANOMALY' if anomaly else 'NORMAL'} event (pt={features[0]:.2f}) into Kafka...")
    producer.send('lhc-raw-events', event)
    producer.flush()

def run_beam_simulator(interval=1.0):
    print(f"📡 LHC STOCHASTIC BEAM SIMULATOR STARTED")
    print("Press Ctrl+C to stop.")
    
    try:
        while True:
            # Generate normal physics signal (Gaussian noise around 0,1 as trained)
            # We scale these slightly to simulate realistic GeV ranges while staying 'normal'
            features = np.random.normal(loc=0, scale=0.5, size=5).tolist()
            is_anomaly = False

            # 10% chance of a stochastic outlier (Anomaly)
            if np.random.random() < 0.10:
                is_anomaly = True
                # Shift features significantly to trigger the Isolation Forest
                features = np.random.normal(loc=5.0, scale=2.0, size=5).tolist()

            inject_physics_event(features, anomaly=is_anomaly)
            time.sleep(interval)
    except KeyboardInterrupt:
        print("\n🛑 Simulator stopped.")

if __name__ == "__main__":
    # Start the continuous beam simulation
    run_beam_simulator()
