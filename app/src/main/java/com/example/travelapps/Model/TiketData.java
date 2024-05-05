package com.example.travelapps.Model;

import java.io.Serializable;
import java.util.Date;

public class TiketData implements Serializable {
    private String id;
    private String asal;
    private String tujuan;
    private Date tanggal;
    private String waktu;
    private double harga;
    private String jumlahPenumpang;
    private String status;

    public TiketData(String id, String asal, String tujuan, Date tanggal, String waktu, double harga, String jumlahPenumpang, String status) {
        this.id = id;
        this.asal = asal;
        this.tujuan = tujuan;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.harga = harga;
        this.jumlahPenumpang = jumlahPenumpang;
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

    public Date getTanggal() {
        return tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public double getHarga() {
        return harga;
    }

    public String getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public String getStatus() {
        return status;
    }
}

