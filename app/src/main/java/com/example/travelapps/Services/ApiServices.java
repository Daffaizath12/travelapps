package com.example.travelapps.Services;

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
import com.example.travelapps.Model.Kota;
import com.example.travelapps.Model.Notifikasi;
import com.example.travelapps.Model.Pemesanan;
import com.example.travelapps.Model.Perjalanan;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApiServices {
    private static String HOST = "http://192.168.0.117/ProjectTA/api/";

    public static String getHOST() {
        return HOST;
    }

    public interface LoginResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface RegisterResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface PerjalananResponseListener {
        void onSuccess(List<TiketData> tiketData);
        void onError(String message);
    }

    public interface UserResponseListener {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface PemesananResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }
    public interface ShowPemesananResponseListener {
        void onSuccess(List<Pemesanan.PemesananData> pemesananDataList);
        void onError(String message);
    }

    public interface AddLatlongResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface CheckLatlongResponseListener {
        void onResult(boolean success, double latitude, double longitude);
        void onError(String message);
    }

    public interface NotifikasiResponseListener {
        void onSuccess(List<Notifikasi> notifikasiList);
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
                    Boolean success = jsonObject.getBoolean("success");
                    if (message.equals("Berhasil Login")){
                        String token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("isLogin", true);
                        editor.putString("token", token);
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
    public static void notifikasi(Context context, String id_user, String title, String desc ,RegisterResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "notifikasi-store.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        listener.onSuccess("Berhasil store notifikasi");
                    } else {
                        listener.onError("Gagal mengirim notifikasi: ");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            listener.onError("Gagal mengirim notif: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("title", title);
                params.put("desc", desc);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getNotifikasi(Context context, String id_user, NotifikasiResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOST + "get-notifikasi.php?id_user=" + id_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<Notifikasi> notifikasiList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String desc = jsonObject.getString("desc");
                                String createdAtString = jsonObject.getString("created_at");
                                Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdAtString);
                                Notifikasi notifikasi = new Notifikasi(title, desc, createdAt);
                                notifikasiList.add(notifikasi);
                            }
                            listener.onSuccess(notifikasiList);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            listener.onError("Failed to parse response: " + e.getMessage());
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
                                listener.onError("Failed to parse error response: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Network error: " + error.toString());
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void showPerjalanan(Context context,String kotaAsal, String kotaTujuan, final PerjalananResponseListener listener) {
        String url = HOST + "schedule.php?kota_asal=" + kotaAsal + "&kota_tujuan=" + kotaTujuan ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.e("response", response);
                                Log.e("url", url);
                                List<TiketData> tiketDataList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(i);
                                    String id = jsonArrayJSONObject.getString("id_perjalanan");
                                    String asal = jsonArrayJSONObject.getString("kota_asal");
                                    String tujuan = jsonArrayJSONObject.getString("kota_tujuan");
                                    String tanggal = jsonArrayJSONObject.getString("tanggal");
                                    String waktu = jsonArrayJSONObject.getString("waktu_keberangkatan");
                                    Double harga = jsonArrayJSONObject.getDouble("harga");
                                    String jumlahPenumpang = jsonArrayJSONObject.getString("jumlah_penumpang");
                                    String status = jsonArrayJSONObject.getString("status");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date ntanggal;
                                    try {
                                        ntanggal = sdf.parse(tanggal);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        continue;
                                    }
                                    TiketData tiketData = new TiketData(id, asal, tujuan,ntanggal, waktu, harga,jumlahPenumpang, status);

                                    tiketDataList.add(tiketData);
                                }
                                listener.onSuccess(tiketDataList);
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
                                listener.onError("Gagal mendapatkan data perjalanan: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data perjalanan: network response is null");
                        }
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getUserData(Context context, String token, final UserResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOST + "getuserbyid.php?token=" + token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("user", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONObject userObj = jsonObject.getJSONObject("user");
                                String id = userObj.getString("id_user");
                                String nama = userObj.getString("nama_lengkap");
                                String notelp = userObj.getString("notelp");
                                String alamat = userObj.getString("alamat");
                                String email = userObj.getString("email");
                                String longitude = userObj.getString("longitude");
                                String latitude = userObj.getString("latitude");
                                User user = new User(id, nama,notelp,email,alamat,latitude, longitude);
                                listener.onSuccess(user);
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
                                listener.onError("Gagal mendapatkan data user: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data user: network response is null");
                        }
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public static void updateUser(Context context, String token, String nama_lengkap, String notelp, String email, String alamat, final UpdateUserResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "update-user.php",
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
                params.put("token", token);
                params.put("nama_lengkap", nama_lengkap);
                params.put("notelp", notelp);
                params.put("email", email);
                params.put("alamat", alamat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface UpdateUserResponseListener {
        void onUpdateUserResponse(boolean success, String message);
    }

    public static void addLatlong(Context context, String token, double latitude, double longitude, AddLatlongResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "addlatLng.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        listener.onSuccess("Latitude dan Longitude berhasil disimpan");
                    } else {
                        String error = jsonObject.getString("error");
                        listener.onError("Gagal menyimpan Latitude dan Longitude: " + error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal menyimpan Latitude dan Longitude: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal menyimpan Latitude dan Longitude: " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void checkLatlong(Context context, String token, CheckLatlongResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "checkLatLng.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");
                        listener.onResult(true, latitude, longitude);
                    } else {
                        listener.onResult(false, 0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal memeriksa Latitude dan Longitude: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal memeriksa Latitude dan Longitude: " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void pemesanan(Context context, String id_user, String id_perjalanan, String qty, String order_id, String alamat_jemput, String alamat_tujuan, String waktu_jemput,String status, String tglBerangkat, String harga , PemesananResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "pesanan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)) {
                        listener.onSuccess("Berhasil melakukan pemesanan tiket");
                    } else {
                        listener.onError("Gagal pesan: " + jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal pesan: " + e.getMessage());
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
                                    String message = jsonObject.getString("error");
                                    if (message.equals("Failed to insert pemesanan")) {
                                        listener.onError("Failed to insert pemesanan");
                                    }
                                } catch (JSONException  e) {
                                    e.printStackTrace();
                                    listener.onError("Gagal pesan: " + e.getMessage());
                                }
                            } catch ( UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        } else {
                            listener.onError("Gagal pesan: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("id_perjalanan", id_perjalanan);
                params.put("order_id", order_id);
                params.put("alamat_jemput", alamat_jemput);
                params.put("alamat_tujuan", alamat_tujuan);
                params.put("waktu_jemput", waktu_jemput);
                params.put("status", status);
                params.put("qty", qty);
                params.put("tanggal_berangkat", tglBerangkat);
                params.put("harga", harga);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void status(Context context, String order_id, String status, AddLatlongResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOST + "updateStatus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        listener.onSuccess("Pesanan berhasil");
                    } else {
                        String error = jsonObject.getString("error");
                        listener.onError("Gagal: " + error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Gagal menyimpan Latitude dan Longitude: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Gagal update: " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("order_id", order_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void showPemesanan(Context context, String idUser, ShowPemesananResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HOST + "showpemesanan.php?id_user=" + idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<Pemesanan.PemesananData> pemesananDataList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            // Parse each pemesanan data
                                   String id =  dataObject.getString("id_pemesanan");
                                   String idUser =  dataObject.getString("id_user");
                                   String idPerjalanan =  dataObject.getString("id_perjalanan");
                                   String qty =  dataObject.getString("qty");
                                   String orderId =  dataObject.getString("order_id");
                                   String alamat_jemput =  dataObject.getString("alamat_jemput");
                                   String alamatTujuan = dataObject.getString("alamat_tujuan");
                                    String waktu_jemput = dataObject.getString("waktu_jemput");
                                    String status = dataObject.getString("pemesanan_status");
                                    String tanggalPesan =  dataObject.getString("tanggal_pesan");
                                    String tanggalBerangkat =  dataObject.getString("tanggal_berangkat");
                                    String harga = dataObject.getString("harga");
                                    String kotaAsal =   dataObject.getString("kota_asal");
                                    String kotaTujuan =    dataObject.getString("kota_tujuan");
                                    String tanggal =    dataObject.getString("tanggal");
                                    String waktuKeberangkatan =    dataObject.getString("waktu_keberangkatan");
                                    Pemesanan.PemesananData pemesananData = new Pemesanan.PemesananData(id, idUser, idPerjalanan,qty, orderId, alamat_jemput, alamatTujuan, waktu_jemput, status, tanggalPesan, tanggalBerangkat, harga, kotaAsal, kotaTujuan, tanggal, waktuKeberangkatan);
                            pemesananDataList.add(pemesananData);
                        }
                        listener.onSuccess(pemesananDataList);
                    } else {
                        listener.onError("Failed to fetch pemesanan data: " + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Failed to parse JSON response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError("Failed to fetch pemesanan data: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
