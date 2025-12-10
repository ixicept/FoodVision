from flask import Blueprint, request, jsonify, Flask
from paddleocr import PaddleOCR
import os
import tempfile

app = Flask(__name__)

image_controller = Blueprint('image_controller', __name__, url_prefix='/image')

@image_controller.route('extract-text', methods=['POST'])
def extract_text():

    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400
    
    file = request.files['file']
    
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400
    
    allowed_extensions = {'png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'}
    if '.' not in file.filename or file.filename.rsplit('.', 1)[1].lower() not in allowed_extensions:
        return jsonify({"error": "File type not allowed"}), 400
    
    try:
        temp_file = tempfile.NamedTemporaryFile(delete=False, suffix='.jpg')
        file.save(temp_file.name)
        
        ocr = PaddleOCR()
        result = ocr.ocr(temp_file.name)
        
        extracted_text = [line[1][0] for line in result[0]]
        
        os.unlink(temp_file.name)
        
        return jsonify({
            "success": True,
            "text": extracted_text,
            "message": "Text extracted successfully"
        }), 200
    
    except Exception as e:
        return jsonify({
            "success": False,
            "error": str(e),
            "message": "Error processing image"
        }), 500

app.register_blueprint(image_controller)

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({"status": "healthy"}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5500, debug=False)
