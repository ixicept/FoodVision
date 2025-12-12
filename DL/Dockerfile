FROM python:3.11-slim

WORKDIR /app

# Copy requirements and install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy model directory and prediction script
COPY model/ ./model/
COPY predict.py .

# Expose Flask port
EXPOSE 5000

# Run the prediction service
CMD ["python", "predict.py"]
