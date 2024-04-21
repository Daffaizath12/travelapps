package com.example.travelapps.Model;

public class TiketData {
    private String date;
    private String asal;
    private String tujuan;
    private String waktu;
    private String harga;

    public TiketData(String date, String asal, String tujuan, String waktu, String harga) {
        this.date = date;
        this.asal = asal;
        this.tujuan = tujuan;
        this.waktu = waktu;
        this.harga = harga;
    }

    public String getDate() {
        return date;
    }

    public String getAsal() {
        return asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getHarga() { return harga; }
}

