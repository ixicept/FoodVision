FROM python:3.11-slim

WORKDIR /app

# Copy requirements and install dependencies
COPY requirements.txt .
RUN python -m pip install --upgrade pip \
	&& pip install --no-cache-dir -r requirements.txt

# Copy model directory and prediction script
COPY model/ ./model/
COPY predict.py .

# Expose Flask port
EXPOSE 5000

# Run the prediction service with UTF-8 encoding
CMD ["python", "-X", "utf8", "predict.py"]
