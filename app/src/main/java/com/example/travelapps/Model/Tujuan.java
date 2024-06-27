package com.example.travelapps.Model;

import com.google.android.gms.maps.model.LatLng;

public class Tujuan {
    private LatLng latLngTujuan;
    private double formattedDistanceTujuan;
    private String namaLengkap;

    public Tujuan(LatLng latLngTujuan, double formattedDistanceTujuan, String namaLengkap) {
        this.latLngTujuan = latLngTujuan;
        this.formattedDistanceTujuan = formattedDistanceTujuan;
        this.namaLengkap = namaLengkap;
    }

    public LatLng getLatLngTujuan() {
        return latLngTujuan;
    }

    public double getFormattedDistanceTujuan() {
        return formattedDistanceTujuan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }
}
