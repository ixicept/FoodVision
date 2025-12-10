FROM python:3.11-slim

WORKDIR /app

# Install system dependencies for OpenCV and image processing
RUN apt-get update --allow-unauthenticated && apt-get install ffmpeg libsm6 libxext6  -y

COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

ENV MPLBACKEND=Agg
ENV DISABLE_MODEL_SOURCE_CHECK=True
COPY server.py .
EXPOSE 5500
CMD ["python", "server.py"]



