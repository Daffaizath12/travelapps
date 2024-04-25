package com.example.travelapps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapps.Services.ApiServices;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    TextView tvDaftar;
    Button btnLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvDaftar = findViewById(R.id.tx_daftar);
        btnLogin = findViewById(R.id.buttonLogin);
        tvDaftar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
            } else if(!isValidEmail(email)){
                etEmail.setError("Format email tidak valid");
            } else if (password.isEmpty()){
                etPassword.setError("Password tidak boleh kosong");
            } else if (password.length() < 8) {
                etPassword.setError("Panjang password harus minimal 8 karakter");
            }  else {
                ApiServices.login(LoginActivity.this, email, password, new ApiServices.LoginResponseListener() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (v == tvDaftar){
            Intent i = new Intent(this, DaftarActivity.class);
            startActivity(i);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
