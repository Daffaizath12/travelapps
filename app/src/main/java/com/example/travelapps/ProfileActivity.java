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

import com.example.travelapps.Model.Sopir;
import com.example.travelapps.Model.User;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.sopir.ApiServicesSopir;
import com.example.travelapps.sopir.HomeSopirActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText etNama, etEmail, etTelp, etAlamat, etUsername, etSim;
    AppCompatButton btnSimpan;
    TextView tvUsername, tvEmail, tvSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        onBindView();
        Intent i = getIntent();
        String role = i.getStringExtra("role");
        SharedPreferences preferences = ProfileActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        String id = preferences.getString("id", "");
        if (role.equals("sopir")) {
            tvEmail.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
            ApiServicesSopir.getSopirData(this, id, new ApiServicesSopir.SopirResponseListener() {
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
                    etAlamat.setText(sopir.getAlamat());
                    etSim.setText(sopir.getNoSim());
                }

                @Override
                public void onError(String message) {

                }
            });
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nama = etNama.getText().toString().trim();
                    String username = etUsername.getText().toString().trim();
                    String telp = etTelp.getText().toString().trim();
                    String sim = etSim.getText().toString().trim();
                    String alamat = etAlamat.getText().toString().trim();
                    ApiServicesSopir.updateUser(ProfileActivity.this, id, nama, sim, telp, username, alamat, new ApiServices.UpdateUserResponseListener() {
                        @Override
                        public void onUpdateUserResponse(boolean success, String message) {
                            Intent intent = new Intent(ProfileActivity.this, HomeSopirActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            Log.e("error", message);
                        }
                    });
                }
            });
        } else {
            tvUsername.setVisibility(View.GONE);
            etUsername.setVisibility(View.GONE);
            tvSim.setVisibility(View.GONE);
            etSim.setVisibility(View.GONE);
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
    }

    private void onBindView() {
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etTelp = findViewById(R.id.et_notelp);
        etAlamat = findViewById(R.id.et_alamat);
        etUsername = findViewById(R.id.et_username);
        etSim = findViewById(R.id.et_sim);

        tvEmail = findViewById(R.id.tv_email);
        tvSim = findViewById(R.id.tv_sim);
        tvUsername = findViewById(R.id.tv_username);
        btnSimpan = findViewById(R.id.btn_simpan);

    }
}