import os
import json
import tensorflow as tf
import tensorflow_hub as hub
import numpy as np
import tensorflow.keras as keras
import shutil
import time

from tensorflow.keras.preprocessing import image
from flask import Flask, request, render_template, jsonify

app = Flask(__name__)
app.config['']


model = tf.keras.models.load_model()

@app.route("/")
def hello():
    return 'Hello World'


@app.route("/")
