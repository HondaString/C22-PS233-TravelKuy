from flask import Flask,jsonify,request
import json
app = Flask(__name__)
@app.route('/', methods=['GET'])
def index():
    return '<h1>Hello world!</h1>'

@app.route('/hotel', methods=['GET'])
# membuat function yang menjalankan file hotel.json
def hotel():
    with open('hotel.json') as f:
        data = json.load(f)
    return jsonify(data)

@app.route('/wisata', methods=['GET'])
# membuat function yang menjalankan file hotel.json
def wisata():
    with open('wisata.json') as f:
        data = json.load(f)
    return jsonify(data)

@app.route('/kuliner', methods=['GET'])
# membuat function yang menjalankan file hotel.json
def kuliner():
    with open('kuliner.json') as f:
        data = json.load(f)
    return jsonify(data)
if __name__ == '__main__':
    app.run(debug=True)