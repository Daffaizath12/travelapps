package com.example.travelapps.pelanggan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class OtpActivity extends AppCompatActivity {

    EditText etOtp;
    AppCompatButton btnVeifikasi;
    RequestQueue requestQueue;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        etOtp = findViewById(R.id.et_otp);
        btnVeifikasi = findViewById(R.id.btn_verifikasi);
        requestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        String email = i.getStringExtra("email");
//        Log.e("email", email);
        btnVeifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = etOtp.getText().toString().trim();
                if (!otp.isEmpty()) {
                    Log.e("email", email);
                    verifyOTP(email, otp);
                } else {
                    Toast.makeText(OtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOTP(final String email, final String otp){
        String url = ApiServices.getHOST() + "verifikasi-otp.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                SharedPreferences.Editor editor = OtpActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                                editor.remove("isLogin");
                                editor.remove("token");
                                editor.remove("id");
                                editor.apply();
                                Intent intent = new Intent(OtpActivity.this, NewPasswordActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                Toast.makeText(OtpActivity.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(OtpActivity.this, "OTP verification failed: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OtpActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OtpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("otp", otp);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}