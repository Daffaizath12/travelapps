package com.example.travelapps.pelanggan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    AppCompatButton btnSend;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmail = findViewById(R.id.et_email);
        btnSend = findViewById(R.id.btn_send);
        String email = etEmail.getText().toString().trim();

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (email.isEmpty()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Silahkan masukkan email anda", Toast.LENGTH_SHORT);
                    } else {
                    Intent intent = new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                }
            });

    }
    private void sendResetPasswordRequest() {
        String email = etEmail.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "reset-password.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.e("res", response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
//                        removeTokenFromPrefs();
                        Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPasswordActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ForgotPasswordActivity.this, "Volley error" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}