package com.example.emergencycpr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DroneActivity extends AppCompatActivity {

    TextView textViewDroneInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));

        textViewDroneInfo = findViewById(R.id.textViewDroneInfo);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //run after 3 seconds
                textViewDroneInfo.setText("Dispatching Drone!");
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //run after 6 seconds
                        textViewDroneInfo.setText("Drone Dispatched!");
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //run after 9 seconds
                                textViewDroneInfo.setText("Live Location Tracking....");
                                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //run after 9 seconds
                                        //textViewDroneInfo.setText("Drone has arrived!");
                                        Intent intent = new Intent(DroneActivity.this, CardiacEmergencyActivity.class);
                                        startActivity(intent);
                                    }
                                }, 3000);
                            }
                        }, 3000);
                    }
                }, 3000);
            }
        }, 3000);
    }
}