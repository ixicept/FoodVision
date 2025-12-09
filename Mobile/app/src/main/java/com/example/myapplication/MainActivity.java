package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare the launcher at the top of your Activity/Fragment.
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

        Button btnGallery = findViewById(R.id.btn_pick_gallery); // Example ID
        btnGallery.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void registerPhotoPicker() {
        // Creates a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                // Here, you can handle the selected image URI.
                // For example, you could pass this URI to another Activity.
                // Intent intent = new Intent(this, AnotherActivity.class);
                // intent.setData(uri);
                // startActivity(intent);

                Toast.makeText(this, "Image selected: " + uri.toString(), Toast.LENGTH_LONG).show();

            } else {
                Log.d("PhotoPicker", "No media selected");
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}