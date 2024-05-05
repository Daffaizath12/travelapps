package com.example.travelapps.sopir;

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
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Services.ApiServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiServicesSopir {

    public interface PerjalananResponseListener {
        void onSuccess(List<PemesananSopir> pemesananSopir);
        void onError(String message);
    }
    public interface PerjalananNowResponseListener {
        void onSuccess(List<TiketData> tiketDataList);
        void onError(String message);
    }
    public static void login(Context context, String username, String pass, ApiServices.LoginResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "login_sopir.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("Berhasil Login")){
                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String id = userObject.getString("id_sopir");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("isLoginSopir", true);
                        editor.putString("id", id);
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
                                        if (message.equals("Username dan Password Salah")) {
                                            listener.onError("Username / password anda salah");
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
                params.put("username", username);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void register(Context context, String nama_lengkap, String username, String password, String no_SIM, String notelp, String alamat, ApiServices.RegisterResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "register_sopir.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        listener.onSuccess("Registrasi berhasil");
                    } else {
                        String error = jsonObject.getString("error");
                        listener.onError(error);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    listener.onError("Gagal melakukan registrasi: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal melakukan registrasi: " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", nama_lengkap);
                params.put("username", username);
                params.put("password", password);
                params.put("no_SIM", no_SIM);
                params.put("notelp", notelp);
                params.put("alamat", alamat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void getPerjalananSopir(Context context, String idSopir, PerjalananResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "getperjalanansopir.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<PemesananSopir> pemesananSopirList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idPemesanan = jsonObject.getString("id_pemesanan");
                        String idUser = jsonObject.getString("id_user");
                        String idPerjalanan = jsonObject.getString("id_perjalanan");
                        String orderId = jsonObject.getString("order_id");
                        String alamatJemput = jsonObject.getString("alamat_jemput");
                        String alamatTujuan = jsonObject.getString("alamat_tujuan");
                        String waktuJemput = jsonObject.getString("waktu_jemput");
                        String status = jsonObject.getString("status");
                        String tanggalPesan = jsonObject.getString("tanggal_pesan");
                        String tanggalBerangkat = jsonObject.getString("tanggal_berangkat");
                        String qty = jsonObject.getString("qty");
                        String harga = jsonObject.getString("harga");
                        String namaLengkap = jsonObject.getString("nama_lengkap");
                        String username = jsonObject.getString("username");
                        String notelp = jsonObject.getString("notelp");
                        String email = jsonObject.getString("email");
                        String alamat = jsonObject.getString("alamat");
                        String idRole = jsonObject.getString("id_role");
                        String password = jsonObject.getString("password");
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");
                        String token = jsonObject.getString("token");
                        String kotaAsal = jsonObject.getString("kota_asal");
                        String kotaTujuan = jsonObject.getString("kota_tujuan");
                        String tanggal = jsonObject.getString("tanggal");
                        String waktuKeberangkatan = jsonObject.getString("waktu_keberangkatan");
                        String jumlahPenumpang = jsonObject.getString("jumlah_penumpang");
                        String idSopir = jsonObject.getString("id_sopir");

                        PemesananSopir pemesananSopir = new PemesananSopir(idPemesanan, idUser, idPerjalanan, orderId, alamatJemput, alamatTujuan, waktuJemput, status, tanggalPesan, tanggalBerangkat, qty, harga, namaLengkap, username, notelp, email, alamat, idRole, password, latitude, longitude, token, kotaAsal, kotaTujuan, tanggal, waktuKeberangkatan, jumlahPenumpang, idSopir);
                        pemesananSopirList.add(pemesananSopir);
                    }
                    listener.onSuccess(pemesananSopirList);
                } catch (JSONException e){
                    e.printStackTrace();
                    listener.onError("Failed to parse response: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Failed to retrieve data: " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_sopir", idSopir);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void getPerjalananSopirNow(Context context, String idSopir, String tanggal, PerjalananNowResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiServices.getHOST() + "getdaftar.php?id_sopir=" + idSopir + "&tanggal=" + tanggal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<TiketData> tiketDataList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idPerjalanan = jsonObject.getString("id_perjalanan");
                        String kotaAsal = jsonObject.getString("kota_asal");
                        String kotaTujuan = jsonObject.getString("kota_tujuan");
                        String tanggalPerjalanan = jsonObject.getString("tanggal");
                        String waktuKeberangkatan = jsonObject.getString("waktu_keberangkatan");
                        double harga = jsonObject.getDouble("harga");
                        String status = jsonObject.getString("status");
                        String jumlahPenumpang = jsonObject.getString("jumlah_penumpang");
                        Date ntanggal;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            ntanggal = sdf.parse(tanggal);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            continue;
                        }
                        TiketData tiketData = new TiketData(idPerjalanan, kotaAsal, kotaTujuan, ntanggal, waktuKeberangkatan, harga, jumlahPenumpang, status);
                        tiketDataList.add(tiketData);
                    }
                    listener.onSuccess(tiketDataList);
                } catch (JSONException e){
                    e.printStackTrace();
                    listener.onError("Gagal memproses data: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal mendapatkan data: " + error.getMessage());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
