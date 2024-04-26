package com.example.travelapps.Model;

public class TiketData {
    private String id;
    private String asal;
    private String tujuan;
    private String tanggal;
    private String waktu;
    private double harga;
    private String status;

    public TiketData(String id, String asal, String tujuan, String tanggal, String waktu, double harga, String status) {
        this.id = id;
        this.asal = asal;
        this.tujuan = tujuan;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.harga = harga;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAsal() {
        return asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public double getHarga() {
        return harga;
    }

    public String getStatus() {
        return status;
    }
}

