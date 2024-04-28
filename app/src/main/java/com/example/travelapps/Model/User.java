package com.example.travelapps.Model;

public class User {
    private String id;
    private String nama;
    private String notelp;
    private String email;
    private String alamat;
    private String latitude;
    private String longitude;

    public User(String id, String nama, String notelp, String email, String alamat, String latitude, String longitude) {
        this.id = id;
        this.nama = nama;
        this.notelp = notelp;
        this.email = email;
        this.alamat = alamat;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public String getEmail() {
        return email;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
