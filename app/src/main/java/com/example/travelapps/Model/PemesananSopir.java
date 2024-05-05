package com.example.travelapps.Model;

public class PemesananSopir {
    private String idPemesanan;
    private String idUser;
    private String idPerjalanan;
    private String orderId;
    private String alamatJemput;
    private String alamatTujuan;
    private String waktuJemput;
    private String status;
    private String tanggalPesan;
    private String tanggalBerangkat;
    private String qty;
    private String harga;
    private String namaLengkap;
    private String username;
    private String notelp;
    private String email;
    private String alamat;
    private String idRole;
    private String password;
    private String latitude;
    private String longitude;
    private String token;
    private String kotaAsal;
    private String kotaTujuan;
    private String tanggal;
    private String waktuKeberangkatan;
    private String jumlahPenumpang;
    private String idSopir;

    public PemesananSopir(String idPemesanan, String idUser, String idPerjalanan, String orderId, String alamatJemput, String alamatTujuan, String waktuJemput, String status, String tanggalPesan, String tanggalBerangkat, String qty, String harga, String namaLengkap, String username, String notelp, String email, String alamat, String idRole, String password, String latitude, String longitude, String token, String kotaAsal, String kotaTujuan, String tanggal, String waktuKeberangkatan, String jumlahPenumpang, String idSopir) {
        this.idPemesanan = idPemesanan;
        this.idUser = idUser;
        this.idPerjalanan = idPerjalanan;
        this.orderId = orderId;
        this.alamatJemput = alamatJemput;
        this.alamatTujuan = alamatTujuan;
        this.waktuJemput = waktuJemput;
        this.status = status;
        this.tanggalPesan = tanggalPesan;
        this.tanggalBerangkat = tanggalBerangkat;
        this.qty = qty;
        this.harga = harga;
        this.namaLengkap = namaLengkap;
        this.username = username;
        this.notelp = notelp;
        this.email = email;
        this.alamat = alamat;
        this.idRole = idRole;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        this.kotaAsal = kotaAsal;
        this.kotaTujuan = kotaTujuan;
        this.tanggal = tanggal;
        this.waktuKeberangkatan = waktuKeberangkatan;
        this.jumlahPenumpang = jumlahPenumpang;
        this.idSopir = idSopir;
    }

    // Getter methods

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdPerjalanan() {
        return idPerjalanan;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAlamatJemput() {
        return alamatJemput;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public String getWaktuJemput() {
        return waktuJemput;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggalPesan() {
        return tanggalPesan;
    }

    public String getTanggalBerangkat() {
        return tanggalBerangkat;
    }

    public String getQty() {
        return qty;
    }

    public String getHarga() {
        return harga;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getUsername() {
        return username;
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

    public String getIdRole() {
        return idRole;
    }

    public String getPassword() {
        return password;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getToken() {
        return token;
    }

    public String getKotaAsal() {
        return kotaAsal;
    }

    public String getKotaTujuan() {
        return kotaTujuan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getWaktuKeberangkatan() {
        return waktuKeberangkatan;
    }

    public String getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public String getIdSopir() {
        return idSopir;
    }
}

