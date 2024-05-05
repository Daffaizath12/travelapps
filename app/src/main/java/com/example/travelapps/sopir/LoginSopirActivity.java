package com.example.travelapps.sopir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.HomeActivity;
import com.example.travelapps.LoginActivity;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

public class LoginSopirActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    AppCompatButton btnLogin;
    TextView tvUser, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sopir);
        onBindView();
    }

    private void onBindView(){
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.buttonLogin);
        tvUser = findViewById(R.id.tv_login);
        tvRegister = findViewById(R.id.tx_daftar);

        btnLogin.setOnClickListener(this);
        tvUser.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                etUsername.setError("Username tidak boleh kosong");
            } else if (password.isEmpty()){
                etPassword.setError("Password tidak boleh kosong");
            } else if (password.length() < 8) {
                etPassword.setError("Panjang password harus minimal 8 karakter");
            } else {
                ApiServicesSopir.login(LoginSopirActivity.this, username, password, new ApiServices.LoginResponseListener() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(LoginSopirActivity.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginSopirActivity.this, HomeSopirActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(LoginSopirActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (v == tvUser) {
            Intent intent = new Intent(LoginSopirActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginSopirActivity.this, RegisterSopirActivity.class);
            startActivity(intent);
        }
    }

}