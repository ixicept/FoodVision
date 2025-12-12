package com.example.myapplication.api;


import com.example.myapplication.models.AnalysisResult;
import com.example.myapplication.models.ItemsNutrient;
import com.example.myapplication.models.ScanResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AnalysisApi {

    public static AnalysisResult analyze(ScanResult scan) {
        ItemsNutrient n = ItemsNutrient.fromText(scan.rawText);

        try {
            JSONObject payload = new JSONObject();
            payload.put("ingredients", scan.rawText);
            payload.put("energy_kcal", n.energy_kcal);
            payload.put("fat", n.fat);
            payload.put("saturated_fat", n.saturated_fat);
            payload.put("carbohydrates", n.carbohydrates);
            payload.put("sugars", n.sugars);
            payload.put("proteins", n.proteins);
            payload.put("fiber", n.fiber);
            payload.put("salt", n.salt);
            payload.put("sodium", n.sodium);

            URL url = new URL("https://nondefinitive-paleontographical-sawyer.ngrok-free.dev/predict");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(payload.toString().getBytes("UTF-8"));
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String prediction = jsonResponse.getString("prediction");

            String message;
            if (prediction.equals("Healthy")) {
                message = "This product is healthy and fits the recommended limits.";
            } else if (prediction.equals("Medium")) {
                message = "This product is moderately healthy.";
            } else {
                message = "This product is not healthy and exceeds recommended limits.";
            }

            return new AnalysisResult(prediction, message);

        } catch (Exception e) {
            e.printStackTrace();
            return new AnalysisResult("Error", "Failed to analyze: " + e.getMessage());
        }
    }
}
