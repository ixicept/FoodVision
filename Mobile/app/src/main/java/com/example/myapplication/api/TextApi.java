package com.example.myapplication.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class TextApi {

    private static final String TAG = "TextApi";
    // For Android Emulator use: http://10.0.2.2:5500
    // For physical device use: http://<YOUR_COMVIS_HOST_IP>:5500
    // Current Wi‑Fi IP of your PC: 10.20.176.217
    private static final String BASE_URL = "http://10.20.176.217:5500";

    public static String extractTextFromUri(Context context, Uri uri) throws IOException {
        byte[] imageBytes = readBytes(context, uri);
        return uploadImageForText(imageBytes);
    }

    private static String uploadImageForText(byte[] imageBytes) throws IOException {
        String boundary = "Boundary-" + UUID.randomUUID();
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        String endpoint = BASE_URL + "/image/extract-text";
        Log.d(TAG, "Uploading image to: " + endpoint + " (" + imageBytes.length + " bytes)");
        
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"upload.jpg\"" + lineEnd);
        dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
        dos.writeBytes(lineEnd);
        dos.write(imageBytes);
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        dos.flush();
        dos.close();

        int status = conn.getResponseCode();
        Log.d(TAG, "Server response status: " + status);
        
        InputStream is = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();
        String response = readStream(is);
        Log.d(TAG, "Server response: " + response);
        conn.disconnect();

        if (status >= 200 && status < 300) {
            String extracted = parseTextArray(response);
            Log.d(TAG, "Extracted text: " + extracted);
            return extracted != null ? extracted : "";
        } else {
            Log.e(TAG, "Server error: " + status + " -> " + response);
            throw new IOException("Comvis service error " + status + ": " + response);
        }
    }

    private static byte[] readBytes(Context context, Uri uri) throws IOException {
        InputStream in = context.getContentResolver().openInputStream(uri);
        if (in == null) throw new IOException("Cannot open input stream");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int nRead;
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        in.close();
        return buffer.toByteArray();
    }

    private static String readStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private static String parseTextArray(String json) {
        int textIdx = json.indexOf("\"text\"");
        if (textIdx == -1) return null;
        int bracket = json.indexOf('[', textIdx);
        int endBracket = json.indexOf(']', bracket);
        if (bracket == -1 || endBracket == -1) return null;
        String arrayContent = json.substring(bracket + 1, endBracket);
        int firstQuote = arrayContent.indexOf('"');
        if (firstQuote == -1) return null;
        int secondQuote = arrayContent.indexOf('"', firstQuote + 1);
        if (secondQuote == -1) return null;
        return arrayContent.substring(firstQuote + 1, secondQuote);
    }
}