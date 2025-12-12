package com.example.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.AnalysisApi;
import com.example.myapplication.api.TextApi;
import com.example.myapplication.models.AnalysisResult;
import com.example.myapplication.models.ScanResult;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerPhotoPicker();

        Button btnScan = findViewById(R.id.btn_start_scan);
        btnScan.setOnClickListener(v -> {
            Intent i = new Intent(this, ScanActivity.class);
            startActivity(i);
        });

        Button btnGallery = findViewById(R.id.btn_pick_gallery); 
        btnGallery.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void registerPhotoPicker() {
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                
                handleSelectedImage(uri);

            } else {
                Log.d("PhotoPicker", "No media selected");
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSelectedImage(Uri uri) {
        Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                String extractedText = TextApi.extractTextFromUri(this, uri);
                ScanResult scanResult = new ScanResult(extractedText);
                AnalysisResult analysis = AnalysisApi.analyze(scanResult);

                runOnUiThread(() -> {
                    Intent i = new Intent(this, ResultActivity.class);
                    i.putExtra("prediction", analysis.prediction);
                    i.putExtra("message", analysis.message);
                    startActivity(i);
                });

            } catch (Exception e) {
                Log.e("MainActivity", "Error processing image: " + e.getMessage(), e);
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}