package com.example.emergencycpr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {


    private EditText editName, editAge, editAddress, editMobile;
    private RadioGroup genderGroup;
    private ImageView imgPhoto;
    private Button btnUploadPhoto, btnRegister;

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Show Status Bar.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(Color.parseColor("#0000bb"));


        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editAddress = findViewById(R.id.editAddress);
        editMobile = findViewById(R.id.editMobile);
        genderGroup = findViewById(R.id.genderGroup);
        imgPhoto = findViewById(R.id.imgPhoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnRegister = findViewById(R.id.btnRegister);

        // Upload Photo
        btnUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Register button
        btnRegister.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String mobile = editMobile.getText().toString().trim();

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            String gender = "";
            if (selectedGenderId != -1) {
                RadioButton selectedGender = findViewById(selectedGenderId);
                gender = selectedGender.getText().toString();
            }

            if (name.isEmpty() || age.isEmpty() || address.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", name);
                editor.apply();

                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}