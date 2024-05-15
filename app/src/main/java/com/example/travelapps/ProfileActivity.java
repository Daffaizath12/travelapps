package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.User;
import com.example.travelapps.Services.ApiServices;

public class ProfileActivity extends AppCompatActivity {

    EditText etNama, etEmail, etTelp, etAlamat;
    AppCompatButton btnSimpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        onBindView();
        SharedPreferences preferences = ProfileActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        ApiServices.getUserData(this, token, new ApiServices.UserResponseListener() {
            @Override
            public void onSuccess(User user) {
                TextView tvname = findViewById(R.id.userName);
                tvname.setText(user.getNama());
                TextView tvNickname = findViewById(R.id.profileImage);
                String twoInitials = user.getNama().substring(0, 2);
                twoInitials = twoInitials.toUpperCase();
                tvNickname.setText(twoInitials);
                etNama.setText(user.getNama());
                etEmail.setText(user.getEmail());
                etTelp.setText(user.getNotelp());
                etAlamat.setText(user.getAlamat());
            }

            @Override
            public void onError(String message) {

            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiServices.updateUser(ProfileActivity.this, token, etNama.getText().toString().trim(), etTelp.getText().toString().trim(), etEmail.getText().toString().trim(), etAlamat.getText().toString().trim(), new ApiServices.UpdateUserResponseListener() {
                    @Override
                    public void onUpdateUserResponse(boolean success, String message) {
                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.e("error", message);
                    }
                });
            }
        });
    }

    private void onBindView() {
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etTelp = findViewById(R.id.et_notelp);
        etAlamat = findViewById(R.id.et_alamat);
        btnSimpan = findViewById(R.id.btn_simpan);
    }
}