package com.example.emergencycpr;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MetronomeActivity extends AppCompatActivity {


    private Button startButton, stopButton;
    private Handler handler = new Handler();
    private boolean isRunning = false;

    // 100 BPM -> 60000 ms / 100 = 600 ms per beat
    private final int intervalMs = 600;

    private final Runnable metronomeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                playClick();
                handler.postDelayed(this, intervalMs);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metronome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        startButton = findViewById(R.id.startMetronomeButton);
        stopButton = findViewById(R.id.stopMetronomeButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMetronome();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMetronome();
            }
        });
    }

    private void startMetronome() {
        if (!isRunning) {
            isRunning = true;
            handler.post(metronomeRunnable);
        }
    }

    private void stopMetronome() {
        isRunning = false;
        handler.removeCallbacks(metronomeRunnable);
    }

    private void playClick() {
        // Simple beep sound using ToneGenerator
        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMetronome();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMetronome();
    }

    @Override
    protected void onStop() {
        stopMetronome();
        super.onStop();
    }
}