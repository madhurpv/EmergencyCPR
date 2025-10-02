package com.example.emergencycpr;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class IsRespondingActivity extends AppCompatActivity {

    Button yesButton, noButton;

    private VideoView videoView;
    private boolean userStartedVideo = false; // track if user pressed play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_is_responding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));



        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAmbulancePopup();
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
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.personresponding;
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

    private void callAmbulancePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call Ambulance");
        builder.setMessage("Do you want to call the ambulance (108)?");

        // YES button → open new activity
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri number = Uri.parse("tel:108");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                //Intent intent = new Intent(IsRespondingActivity.this, IsBreathingActivity.class);
                //startActivity(intent);
                //Toast.makeText(MainActivity.this, "Opening New Activity!", Toast.LENGTH_SHORT).show();
            }
        });

        // NO button → just dismiss dialog
        builder.setNeutralButton("No, I have already called", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(IsRespondingActivity.this, IsBreathingActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        // NO button → just dismiss dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(IsRespondingActivity.this, IsBreathingActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}