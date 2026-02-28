import json
import time
from kafka import KafkaProducer

def inject_physics_event(pt=45.0, anomaly=False):
    producer = KafkaProducer(
        bootstrap_servers=['localhost:9092'],
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )
    
    event = {
        "event_id": int(time.time()),
        "pt": 1500.2 if anomaly else pt,
        "eta": 0.5,
        "phi": 1.2,
        "met": 300.5 if anomaly else 10.0
    }
    
    print(f"🚀 Injecting {'ANOMALY' if anomaly else 'NORMAL'} event into Kafka...")
    producer.send('lhc-raw-events', event)
    producer.flush()
    print("✅ Event sent to wire.")

if __name__ == "__main__":
    # Inject one normal and one anomaly
    inject_physics_event(pt=42.5, anomaly=False)
    time.sleep(2)
    inject_physics_event(anomaly=True)
