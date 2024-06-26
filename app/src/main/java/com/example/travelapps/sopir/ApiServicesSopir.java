package com.example.travelapps.sopir;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.Sopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.TiketSopir;
import com.example.travelapps.Model.User;
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
    public interface TiketSopirResponseListener {
        void onSuccess(List<TiketSopir> tiketDataList);
        void onError(String message);
    }


    public interface UpdateStatusResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface  SopirResponseListener{
        void onSuccess(Sopir sopir);
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
                    Boolean success = jsonObject.getBoolean("success");
                    if (message.equals("Berhasil Login")){
                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String id = userObject.getString("id_sopir");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("isLoginSopir", true);
                        editor.putString("id", id);
                        editor.apply();
                        listener.onSuccess(message);
                    } else if (success.equals(false)) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
    public static void getPenumpangSopir(Context context, String idSopir,String id_perjalanan, PerjalananResponseListener listener) {
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
                        String idmobil = jsonObject.getString("mobil_id");
                        String latTujuan = jsonObject.getString("lat_tujuan");
                        String lngTujuan = jsonObject.getString("lng_tujuan");
                        String jemput = jsonObject.getString("status_penjemputan");
                        String antar = jsonObject.getString("status_antar");

                        PemesananSopir pemesananSopir = new PemesananSopir(idPemesanan, idUser, idPerjalanan, orderId, alamatJemput, alamatTujuan, waktuJemput, status, tanggalPesan, tanggalBerangkat, qty, harga, namaLengkap, username, notelp, email, alamat, idRole, password, latitude, longitude, token, kotaAsal, kotaTujuan, tanggal, waktuKeberangkatan, jumlahPenumpang, idSopir, idmobil, latTujuan, lngTujuan, jemput, antar);
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
                params.put("id_perjalanan", id_perjalanan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void getPenumpangSopirActive(Context context, String idSopir,String id_perjalanan, PerjalananResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "getperjalanansopiractive.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    if (message.equals("ok")) {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");
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
                            String idmobil = jsonObject.getString("mobil_id");
                            String latTujuan = jsonObject.getString("lat_tujuan");
                            String lngTujuan = jsonObject.getString("lng_tujuan");
                            String jemput = jsonObject.getString("status_penjemputan");
                            String antar = jsonObject.getString("status_antar");



                            PemesananSopir pemesananSopir = new PemesananSopir(idPemesanan, idUser, idPerjalanan, orderId, alamatJemput, alamatTujuan, waktuJemput, status, tanggalPesan, tanggalBerangkat, qty, harga, namaLengkap, username, notelp, email, alamat, idRole, password, latitude, longitude, token, kotaAsal, kotaTujuan, tanggal, waktuKeberangkatan, jumlahPenumpang, idSopir, idmobil, latTujuan, lngTujuan, jemput, antar);
                            pemesananSopirList.add(pemesananSopir);
                        }
                        listener.onSuccess(pemesananSopirList);
                    } else if(message.equals("Data tidak ditemukan")) {
                        listener.onError("Data tidak ditemukan");
                    }
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
                params.put("id_perjalanan", id_perjalanan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void getPerjalananSopirNow(Context context, String idSopir, PerjalananNowResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiServices.getHOST() + "getdaftar.php?id_sopir=" + idSopir, new Response.Listener<String>() {
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
                            ntanggal = sdf.parse(tanggalPerjalanan);
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
    public static void getTiketSopir(Context context, String idSopir, TiketSopirResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiServices.getHOST() + "getstoryperjalanan.php?id_sopir=" + idSopir, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<TiketSopir> tiketDataList = new ArrayList<>();
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
                        String totalPenumpang = jsonObject.getString("total_penumpang");
                        Date ntanggal;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            ntanggal = sdf.parse(tanggalPerjalanan);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            continue;
                        }
                        TiketSopir tiketData = new TiketSopir(idPerjalanan, kotaAsal, kotaTujuan, ntanggal, waktuKeberangkatan, harga, jumlahPenumpang, status, totalPenumpang);
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
    public static void updateStatus(Context context, String idPemesanan, UpdateStatusResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "update-status-penjemputan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onSuccess(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Gagal memproses response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal mengirim request: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pemesanan", idPemesanan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void tujuanStatus(Context context, String idPemesanan, UpdateStatusResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "status-antar-sopir.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            listener.onSuccess(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Gagal memproses response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal mengirim request: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pemesanan", idPemesanan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getSopirData(Context context, String token, final SopirResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiServices.getHOST() + "getsopirbyid.php?id=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("sopir", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONObject userObj = jsonObject.getJSONObject("sopir");
                                String id = userObj.getString("id_sopir");
                                String nama = userObj.getString("nama_lengkap");
                                String notelp = userObj.getString("notelp");
                                String alamat = userObj.getString("alamat");
                                String username = userObj.getString("username");
                                String noSim = userObj.getString("no_SIM");
                                String active = userObj.getString("status");
                                Sopir sopir = new Sopir(id, nama, username, noSim, notelp,alamat, active);
                                listener.onSuccess(sopir);
                            }
                        } catch (JSONException e){
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
                                listener.onError("Gagal mendapatkan data sopir: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data sopir: network response is null");
                        }
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void updateUser(Context context, String token, String nama_lengkap, String sim, String notelp, String username, String alamat, final ApiServices.UpdateUserResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiServices.getHOST() + "update-sopir.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            listener.onUpdateUserResponse(success, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUpdateUserResponse(false, "Failed to parse response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onUpdateUserResponse(false, "Failed to update user: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_sopir", token);
                params.put("nama_lengkap", nama_lengkap);
                params.put("notelp", notelp);
                params.put("username", username);
                params.put("alamat", alamat);
                params.put("no_SIM", sim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
