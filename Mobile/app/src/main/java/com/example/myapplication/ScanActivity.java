package com.example.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.myapplication.api.AnalysisApi;
import com.example.myapplication.api.TextApi;
import com.example.myapplication.models.AnalysisResult;
import com.example.myapplication.models.ScanResult;
import com.example.myapplication.utils.CameraUtils;
import com.example.myapplication.BuildConfig;

import java.io.File;


public class ScanActivity extends AppCompatActivity {

    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        registerCameraLauncher();

        if (savedInstanceState == null) {
            launchCamera();
        }

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void registerCameraLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result && imageUri != null) {
                        Log.d("ScanActivity", "Picture taken. URI: " + imageUri);

                        handleScannedImage(imageUri);

                    } else {
                        Log.d("ScanActivity", "Scan canceled.");
                        finish();
                    }
                }
        );
    }

    private void launchCamera() {
        try {
            File imageDir = new File(getCacheDir(), "images");
            if (!imageDir.exists()) imageDir.mkdirs();

            File imageFile = new File(imageDir, "scan_image.jpg");

            // authority must match manifest provider
            String authority = BuildConfig.APPLICATION_ID + ".provider";

            imageUri = FileProvider.getUriForFile(this, authority, imageFile);

            takePictureLauncher.launch(imageUri);

        } catch (Exception e) {
            Log.e("ScanActivity", "Camera launch error: " + e.getMessage());
            Toast.makeText(this, "Cannot open camera.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void handleScannedImage(Uri uri) {
        Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show();

        // Run network call on background thread
        new Thread(() -> {
            try {
                // Step 1: extract text using your API
                String extractedText = TextApi.extractTextFromUri(this, uri);

                ScanResult scanResult = new ScanResult(extractedText);

                // Step 2: analyze the nutrients using your API
                AnalysisResult analysis = AnalysisApi.analyze(scanResult);

                // Step 3: navigate to ResultActivity (must run on main thread)
                runOnUiThread(() -> {
                    Intent i = new Intent(this, ResultActivity.class);
                    i.putExtra("healthy", analysis.isHealthy);
                    i.putExtra("message", analysis.message);
                    startActivity(i);
                    finish();
                });

            } catch (Exception e) {
                Log.e("ScanActivity", "Error processing image: " + e.getMessage(), e);
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}