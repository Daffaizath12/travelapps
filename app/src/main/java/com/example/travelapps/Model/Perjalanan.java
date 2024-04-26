package com.example.travelapps.Model;

import java.io.Serializable;

public class Perjalanan implements Serializable {
    private String id;
    private String asal;
    private String tujuan;
    private String waktu;
    private double harga;
    private String status;

    public Perjalanan(String id, String asal, String tujuan, String waktu, double harga, String status) {
        this.id = id;
        this.asal = asal;
        this.tujuan = tujuan;
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

    public String getWaktu() {
        return waktu;
    }

    public Double getHarga() {
        return harga;
    }

    public String getStatus() {
        return status;
    }
}
