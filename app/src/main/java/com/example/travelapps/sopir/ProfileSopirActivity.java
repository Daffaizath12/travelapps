package com.example.travelapps.sopir;

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

import com.example.travelapps.HomeActivity;
import com.example.travelapps.Model.Sopir;
import com.example.travelapps.ProfileActivity;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

public class ProfileSopirActivity extends AppCompatActivity {
    EditText etNama, etUsername, etTelp, etAlamat, etSim;
    AppCompatButton btnSimpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sopir);
        onBindView();
        SharedPreferences preferences = ProfileSopirActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("id", "");
        ApiServicesSopir.getSopirData(ProfileSopirActivity.this, token, new ApiServicesSopir.SopirResponseListener() {
            @Override
            public void onSuccess(Sopir sopir) {
                TextView tvname = findViewById(R.id.userName);
                tvname.setText(sopir.getNamaLengkap());
                TextView tvNickname = findViewById(R.id.profileImage);
                String twoInitials = sopir.getNamaLengkap().substring(0, 2);
                twoInitials = twoInitials.toUpperCase();
                tvNickname.setText(twoInitials);
                etNama.setText(sopir.getNamaLengkap());
                etUsername.setText(sopir.getUsername());
                etTelp.setText(sopir.getNoTelp());
                etSim.setText(sopir.getNoSim());
                etAlamat.setText(sopir.getAlamat());
            }

            @Override
            public void onError(String message) {

            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiServicesSopir.updateUser(ProfileSopirActivity.this, token, etNama.getText().toString().trim(), etSim.getText().toString().trim(), etTelp.getText().toString().trim(), etUsername.getText().toString().trim(), etAlamat.getText().toString().trim(), new ApiServices.UpdateUserResponseListener() {
                    @Override
                    public void onUpdateUserResponse(boolean success, String message) {
                        Intent intent = new Intent(ProfileSopirActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ProfileSopirActivity.this, message, Toast.LENGTH_SHORT).show();
                        Log.e("error", message);
                    }
                });
            }
        });
    }
    private void onBindView() {
        etNama = findViewById(R.id.et_nama);
        etUsername = findViewById(R.id.et_username);
        etTelp = findViewById(R.id.et_notelp);
        etSim = findViewById(R.id.et_sim);
        etAlamat = findViewById(R.id.et_alamat);
        btnSimpan = findViewById(R.id.btn_simpan);
    }
}