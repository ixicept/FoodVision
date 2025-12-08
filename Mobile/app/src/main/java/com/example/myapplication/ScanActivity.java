package com.example.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.AnalysisApi;
import com.example.myapplication.api.TextApi;
import com.example.myapplication.models.AnalysisResult;
import com.example.myapplication.models.ScanResult;
import com.example.myapplication.utils.CameraUtils;


public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_scan);

        CameraUtils.openCamera(this, bitmap -> {
            String labelText = TextApi.extractText(bitmap);
            ScanResult scan = new ScanResult(labelText);

            AnalysisResult result = AnalysisApi.analyze(scan);

            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("healthy", result.isHealthy);
            i.putExtra("message", result.message);
            startActivity(i);
            finish();
        });
    }
}