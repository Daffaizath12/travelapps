package com.example.travelapps.sopir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.DaftarActivity;
import com.example.travelapps.LoginActivity;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.R;

public class RegisterSopirActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etNama, etPassword, etSim, etTelp, etAlamat;
    TextView tvLogin;
    AppCompatButton btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sopir);
        onBindView();
    }

    private void onBindView(){
        etUsername = findViewById(R.id.et_username);
        etNama = findViewById(R.id.et_nama);
        etPassword = findViewById(R.id.et_password);
        etSim = findViewById(R.id.et_sim);
        etTelp = findViewById(R.id.et_telp);
        etAlamat = findViewById(R.id.et_alamat);
        btnDaftar = findViewById(R.id.buttonRegister);
        tvLogin = findViewById(R.id.tx_login);

        btnDaftar.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnDaftar) {
            String username = etUsername.getText().toString().trim();
            String nama = etNama.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String sim = etSim.getText().toString().trim();
            String telp = etTelp.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();

            if (username.isEmpty()) {
                etUsername.setError("Username tidak boleh kosong");
            } else if (nama.isEmpty()) {
                etNama.setError("Nama tidak boleh kosong");
            } else if (password.isEmpty()){
                etPassword.setError("Password tidak boleh kosong");
            } else if (password.length() < 8) {
                etPassword.setError("Panjang password harus minimal 8 karakter");
            } else if (sim.isEmpty()) {
                etSim.setError("Nomor SIM tidak boleh kosong");
            } else if (telp.isEmpty()) {
                etTelp.setError("Nomor telepon tidak boleh kosong");
            } else if (alamat.isEmpty()) {
                etAlamat.setError("Alamat tidak boleh kosong");
            } else {
              ApiServicesSopir.register(RegisterSopirActivity.this, nama, username, password, sim, telp, alamat, new ApiServices.RegisterResponseListener() {
                  @Override
                  public void onSuccess(String message) {
                      Toast.makeText(RegisterSopirActivity.this, "Berhasil Daftar Akun", Toast.LENGTH_LONG).show();
                      Intent i = new Intent(RegisterSopirActivity.this, LoginSopirActivity.class);
                      startActivity(i);
                      finish();
                  }

                  @Override
                  public void onError(String message) {
                      Toast.makeText(RegisterSopirActivity.this, message, Toast.LENGTH_LONG).show();
                      Log.e("Error Register", message);
                  }
              });
            }
        } else {
            Intent i = new Intent(RegisterSopirActivity.this, LoginSopirActivity.class);
            startActivity(i);
        }
    }
}