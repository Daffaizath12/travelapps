package com.example.travelapps.Model;

public class Sopir {
    private String idSopir;
    private String namaLengkap;
    private String username;
    private String noSim;
    private String noTelp;
    private String alamat;
    private String active;

    public Sopir(String idSopir, String namaLengkap, String username, String noSim, String noTelp, String alamat, String active) {
        this.idSopir = idSopir;
        this.namaLengkap = namaLengkap;
        this.username = username;
        this.noSim = noSim;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.active = active;
    }

    public String getIdSopir() {
        return idSopir;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public String getNoSim() {
        return noSim;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getActive() {
        return active;
    }
}
