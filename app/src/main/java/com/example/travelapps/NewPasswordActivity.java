package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelapps.Services.ApiServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {
    EditText etPassword;
    AppCompatButton btnUpdate;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        etPassword = findViewById(R.id.et_password);
        btnUpdate = findViewById(R.id.btn_update);
        requestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        String email = i.getStringExtra("email");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(NewPasswordActivity.this, "Please enter NewPassword", Toast.LENGTH_SHORT).show();
                } else {
                newPassword(email, etPassword.getText().toString().trim());
            }}
        });
    }

    private void newPassword(final String email, final String new_password){
        String url = ApiServices.getHOST() + "new-password.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("hahah", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(NewPasswordActivity.this, "Update password successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(NewPasswordActivity.this, "Update password failed: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewPasswordActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewPasswordActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", new_password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}