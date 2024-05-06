package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.R;

public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDaftar;
    TextView tvLogin;
    EditText etEmail, etNama, etNotelp, etAlamat, etPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        btnDaftar = findViewById(R.id.buttonRegister);
        tvLogin = findViewById(R.id.tx_login);
        etEmail = findViewById(R.id.et_email);
        etNama = findViewById(R.id.et_nama);
        etNotelp = findViewById(R.id.et_telp);
        etAlamat = findViewById(R.id.et_alamat);
        etPassword = findViewById(R.id.et_password);

        btnDaftar.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvLogin) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String notelp = etNotelp.getText().toString().trim();
            String nama = etNama.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
            } else if(!isValidEmail(email)){
                etEmail.setError("Format email tidak valid");
            } else if(notelp.isEmpty()){
                etNotelp.setError("No. Telepon tidak boleh kosong");
            } else if(notelp.length()>13){
                etNotelp.setError("No. Telepon tidak boleh lebih dari 13 angka");
            } else if(alamat.isEmpty()){
                etAlamat.setError("Alamat tidak boleh kosong");
            } else if (password.isEmpty()){
                etPassword.setError("Password tidak boleh kosong");
            } else if (password.length() < 8) {
                etPassword.setError("Panjang password harus minimal 8 karakter");
            } else {
                ApiServices.register(DaftarActivity.this, nama, notelp, email, alamat, password, new ApiServices.RegisterResponseListener() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(DaftarActivity.this, "Berhasil Daftar Akun", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(DaftarActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(DaftarActivity.this, message, Toast.LENGTH_LONG).show();
                        Log.e("Error Register", message);
                    }
                });
            }
        }
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}