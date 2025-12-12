import tensorflow as tf
import numpy as np
from flask import Flask, request, jsonify
from dataclasses import dataclass
# from tensorflow.keras.layers import TextVectorization

import os
os.environ["PYTHONIOENCODING"] = "utf-8"
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"


model = tf.keras.models.load_model("./model/model_cleaned.keras", compile=False)
class_names = ["Healthy", "Medium", "Not Healthy"]

@dataclass
class Product:
    ingredients: str
    energy_kcal: float
    fat: float
    saturated_fat: float
    carbohydrates: float
    sugars: float
    proteins: float
    fiber: float
    salt: float
    sodium: float

app = Flask(__name__)

# def load_vocab(path):
#     with open(path, "r", encoding="utf-8") as f:
#         return [w.strip() for w in f.readlines()]

# text_vocab = load_vocab("./model/text_vocab.txt")

# text_vectorizer = tf.keras.layers.TextVectorization(
#     max_tokens=len(text_vocab),
#     output_sequence_length=200
# )
# text_vectorizer.set_vocabulary(text_vocab)

@app.route('/predict', methods=['POST'])
def predict():

    data = request.get_json()

    req = Product(**data)

    textInput = np.array([req.ingredients], dtype=object)

    numberInput = np.array([
        [
            req.energy_kcal,
            req.fat,
            req.saturated_fat,
            req.carbohydrates,
            req.sugars,
            req.proteins,
            req.fiber,
            req.salt,
            req.sodium
        ]
        # [45.0,1.2,0.2,8.5,4.0,1.1,1.8,0.3,0.12]
    ])

    # text_vec = text_vectorizer(textInput)

    res = model.predict({
        "text_input": textInput,
        "numeric_input": numberInput
    })
    # print(res)

    # Convert to label
    pred_index = np.argmax(res)
    pred_label = class_names[pred_index]

    # print("Prediction :", pred_label)
    # print("Probabilities :", res)

    return jsonify({
        "prediction": pred_label,
        # "probability": res
    })

if __name__ == "__main__":
    print("🚀 Starting server on http://127.0.0.1:5000")
    app.run(debug=True)