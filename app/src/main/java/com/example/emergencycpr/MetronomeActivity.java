package com.example.emergencycpr;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MetronomeActivity extends AppCompatActivity {


    private Button startButton, stopButton;
    private TextView metronomeCounter;
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private int beatCount = 0;

    // 100 BPM -> 60000 ms / 100 = 600 ms per beat
    private final int intervalMs = 600;

    private final Runnable metronomeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                playClick();
                updateCounter();
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

        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));


        startButton = findViewById(R.id.startMetronomeButton);
        stopButton = findViewById(R.id.stopMetronomeButton);
        metronomeCounter = findViewById(R.id.metronomeCounter);

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
            beatCount = 0;
            handler.post(metronomeRunnable);
        }
    }

    private void stopMetronome() {
        isRunning = false;
        handler.removeCallbacks(metronomeRunnable);
    }

    private void playClick() {
        // High beep on the first beat, lower beep on others
        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        if (beatCount == 0) {
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 150); // Strong beat
        } else {
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 100);  // Weak beat
        }
    }

    private void updateCounter() {
        beatCount = (beatCount % 100) + 1; // cycle 1-100
        metronomeCounter.setText(String.valueOf(beatCount));
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