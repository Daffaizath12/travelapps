package com.example.travelapps.Services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelapps.Model.Kota;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiServices {
    private static String HOST = "http://192.168.0.117/ProjectTA/api/";

    public interface LoginResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface RegisterResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface KotaResponseListener {
        void onSuccess(List<Kota> kotaList);
        void onError(String message);
    }

    public static void login(Context context, String email, String pass, LoginResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("Berhasil Login")){
                        String token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("isLogin", true);
                        editor.putString("token", token);
                        editor.apply();
                        listener.onSuccess(message);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                int statusCode = error.networkResponse.statusCode;
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                if (statusCode == 401) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseBody);
                                        String message = jsonObject.getString("message");
                                        if (message.equals("Email dan Password Salah")) {
                                            listener.onError("Email / password anda salah");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        listener.onError("Gagal Login: " + e.getMessage());
                                    }

                                }
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            listener.onError("Gagal Login: network response is null");

                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void register(Context context, String nama, String notelp, String email, String alamat, String password, RegisterResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        listener.onSuccess("Berhasil Register");
                    } else {
                        listener.onError("Gagal register: " + jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal register: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                int statusCode = error.networkResponse.statusCode;
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                try {
                                    JSONObject jsonObject = new JSONObject(responseBody);
                                    String message = jsonObject.getString("message");
                                    if (message.equals("Email sudah terdaftar")) {
                                        listener.onError("Email sudah terdaftar , Silahkan gunakan email yang lain");
                                    }
                                } catch (JSONException  e) {
                                    e.printStackTrace();
                                    listener.onError("Gagal register: " + e.getMessage());
                                }
                            } catch ( UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        } else {
                            listener.onError("Gagal register: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("alamat", alamat);
                params.put("notelp", notelp);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void showKota(Context context, final KotaResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOST + "getcity.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.e("response", response);
                                List<Kota> kotaList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                                    String id = jsonArrayJSONObject.getString("id_kota");
                                    String nama = jsonArrayJSONObject.getString("nama_kota");

                                    Kota kota = new Kota(id, nama);

                                    kotaList.add(kota);

                                }
                                listener.onSuccess(kotaList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan data kota: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data kota: network response is null");
                        }
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
