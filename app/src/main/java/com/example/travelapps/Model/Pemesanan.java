package com.example.travelapps.Model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public interface Pemesanan {
    void onSuccess(List<PemesananData> pemesananDataList);
    void onError(String message);

    class PemesananData implements Serializable {
        private String idPemesanan;
        private String idUser;
        private String idPerjalanan;
        private String qty;
        private String orderId;
        private String alamatJemput;
        private String alamatTujuan;
        private String waktuJemput;
        private String status;
        private String tanggalPesan;
        private String tanggalBerangkat;
        private String harga;
        private String kotaAsal;
        private String kotaTujuan;
        private String tanggal;
        private String waktuKeberangkatan;

        public PemesananData(String idPemesanan, String idUser, String idPerjalanan, String qty, String orderId, String alamatJemput, String alamatTujuan, String waktuJemput, String status, String tanggalPesan, String tanggalBerangkat, String harga, String kotaAsal, String kotaTujuan, String tanggal, String waktuKeberangkatan) {
            this.idPemesanan = idPemesanan;
            this.idUser = idUser;
            this.idPerjalanan = idPerjalanan;
            this.qty = qty;
            this.orderId = orderId;
            this.alamatJemput = alamatJemput;
            this.alamatTujuan = alamatTujuan;
            this.waktuJemput = waktuJemput;
            this.status = status;
            this.tanggalPesan = tanggalPesan;
            this.tanggalBerangkat = tanggalBerangkat;
            this.harga = harga;
            this.kotaAsal = kotaAsal;
            this.kotaTujuan = kotaTujuan;
            this.tanggal = tanggal;
            this.waktuKeberangkatan = waktuKeberangkatan;
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
        public String getQty() {
            return qty;
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

        public String getHarga() {
            return harga;
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

        public void setIdPemesanan(String idPemesanan) {
            this.idPemesanan = idPemesanan;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public void setIdPerjalanan(String idPerjalanan) {
            this.idPerjalanan = idPerjalanan;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setAlamatJemput(String alamatJemput) {
            this.alamatJemput = alamatJemput;
        }

        public void setAlamatTujuan(String alamatTujuan) {
            this.alamatTujuan = alamatTujuan;
        }

        public void setWaktuJemput(String waktuJemput) {
            this.waktuJemput = waktuJemput;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setTanggalPesan(String tanggalPesan) {
            this.tanggalPesan = tanggalPesan;
        }

        public void setTanggalBerangkat(String tanggalBerangkat) {
            this.tanggalBerangkat = tanggalBerangkat;
        }

        public void setHarga(String harga) {
            this.harga = harga;
        }

        public void setKotaAsal(String kotaAsal) {
            this.kotaAsal = kotaAsal;
        }

        public void setKotaTujuan(String kotaTujuan) {
            this.kotaTujuan = kotaTujuan;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public void setWaktuKeberangkatan(String waktuKeberangkatan) {
            this.waktuKeberangkatan = waktuKeberangkatan;
        }
    }
}

