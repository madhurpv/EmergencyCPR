package com.example.emergencycpr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    LinearLayout AEDCardView, CPRCardView, EmergencyCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));


        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("user_name", "");

        if(savedName.isEmpty()){
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }


        AEDCardView = findViewById(R.id.aedButtonLayout);
        CPRCardView = findViewById(R.id.cprButtonLayout);
        EmergencyCardView = findViewById(R.id.emergencyButtonLayout);


        showEmergencyPopup();


        AEDCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CPRCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://youtube.com/playlist?list=PLfqASPDZ7aT_ZI-jhLGAGCy5RbAoLPeja&si=UnJpVMFCONbwYZ_U"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        EmergencyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showEmergencyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Emergency Check");
        builder.setMessage("Are you in an emergency?");

        // YES button → open new activity
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Opening New Activity!", Toast.LENGTH_SHORT).show();
            }
        });

        // NO button → just dismiss dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}