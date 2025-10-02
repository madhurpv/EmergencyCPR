package com.example.emergencycpr;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CardiacEmergencyActivity3 extends AppCompatActivity {


    Button doneButton;

    private VideoView videoView;
    private boolean userStartedVideo = false; // track if user pressed play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cardiac_emergency3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));



        doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardiacEmergencyActivity3.this, MetronomeActivity.class);
                startActivity(intent);
            }
        });





        videoView = findViewById(R.id.videoView);

        videoView.setOnPreparedListener(mp -> {
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();

            int maxWidth = 800;  // or parent.getWidth()
            int maxHeight = 600; // or parent.getHeight()

            float ratio = Math.min((float) maxWidth / videoWidth,
                    (float) maxHeight / videoHeight);

            int newWidth = Math.round(videoWidth * ratio);
            int newHeight = Math.round(videoHeight * ratio);

            videoView.getLayoutParams().width = newWidth;
            videoView.getLayoutParams().height = newHeight;
            videoView.requestLayout();

            // Re-anchor controller after layout adjustment
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            videoView.setMediaController(mc);
        });


        // Set video path (from res/raw)
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.givingcpr;
        Uri uri = Uri.parse(videoPath);

        // MediaController with play/pause/seek controls
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        // Detect if user pressed play
        mediaController.setMediaPlayer(videoView);
        mediaController.setPrevNextListeners(
                v -> userStartedVideo = true,   // next button
                v -> userStartedVideo = true    // prev button
        );

        videoView.setOnPreparedListener(mp -> {
            // if user manually starts
            videoView.setOnTouchListener((v, event) -> {
                userStartedVideo = true;
                return false;
            });
        });

        // Auto-start after 2 seconds if not started manually
        new Handler().postDelayed(() -> {
            if (!userStartedVideo && !videoView.isPlaying()) {
                videoView.start();
            }
        }, 2000);


    }
}