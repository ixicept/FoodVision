package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_result);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity and go back
            }
        });

        boolean healthy = getIntent().getBooleanExtra("healthy", false);
        String msg = getIntent().getStringExtra("message");

        TextView status = findViewById(R.id.txt_health_status);
        TextView desc = findViewById(R.id.txt_health_desc);

        status.setText(healthy ? "Healthy" : "Not Healthy");
        desc.setText(msg);


    }
}