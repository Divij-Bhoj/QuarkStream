import numpy as np
import pandas as pd
from sklearn.ensemble import IsolationForest
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType

def train_and_export():
    """
    Trains a professional Isolation Forest model for anomaly detection
    on physics telemetry and exports it to ONNX format.
    """
    print("--- QuarkStream MLOps: Model Training Phase ---")
    
    # Generate synthetic "normal" physics data (pT, eta, phi, energy, mass)
    # Inside CERN detectors, most events follow predictable kinematic patterns.
    n_samples = 1000
    n_features = 5
    X_train = np.random.normal(loc=0, scale=1, size=(n_samples, n_features))
    
    print(f"Generated {n_samples} training samples with {n_features} features.")

    # Initialize and train Isolation Forest
    # contamination=0.01 means we expect roughly 1% of data to be anomalies.
    model = IsolationForest(contamination=0.01, random_state=42)
    model.fit(X_train)
    
    print("Model training complete.")

    # Convert the scikit-learn model to ONNX
    # Specify target_opset=13 to ensure compatibility
    initial_type = [('float_input', FloatTensorType([None, n_features]))]
    onnx_model = convert_sklearn(
        model, 
        initial_types=initial_type,
        target_opset=13
    )
    
    # Save the model
    model_path = "src/main/resources/model.onnx"
    with open(model_path, "wb") as f:
        f.write(onnx_model.SerializeToString())
    
    print(f"Successfully exported model to: {model_path}")

if __name__ == "__main__":
    train_and_export()
