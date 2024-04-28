package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TambahLokasiJemput extends AppCompatActivity {

    String idPerjalanan = "";
    String asal = "";
    String waktu = "";
    String tanggal = "";
    String tujuan = "";
    String idUser = "";
    String harga = "";
    String namaUser = "";
    String penumpang = "";

    TextView tvAsal, tvTujuan, tvWaktu, tvTanggal, tvNama, tvPenumpang, tvTotal;
    EditText etAlamatTujuan;
    AppCompatButton btnLokasi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lokasi_jemput);

        tvAsal = findViewById(R.id.asal);
        tvTujuan = findViewById(R.id.Tujuan);
        tvWaktu = findViewById(R.id.waktu);
        tvTanggal = findViewById(R.id.date);
        tvPenumpang = findViewById(R.id.txtJumlahPenumpang);
        tvNama = findViewById(R.id.txtpenumpang);
        tvTotal = findViewById(R.id.tvTotal);
        etAlamatTujuan = findViewById(R.id.et_alamat);
        btnLokasi = findViewById(R.id.btnCurrentLoc);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            idPerjalanan = intent.getStringExtra("id_perjalanan");
            idUser = intent.getStringExtra("id_user");
            asal = intent.getStringExtra("asal");
            waktu = intent.getStringExtra("waktu");
            tanggal = intent.getStringExtra("tanggal");

            tujuan = intent.getStringExtra("tujuan");
            harga = intent.getStringExtra("harga");
            namaUser = intent.getStringExtra("nama_user");
            penumpang = intent.getStringExtra("penumpang");

            tvAsal.setText(asal);
            tvWaktu.setText(waktu);
            tvTanggal.setText(tanggal);
            tvTujuan.setText(tujuan);


            String hargaCleaned = harga.replaceAll("[^\\d.]", "");

            double hargaDouble = Double.parseDouble(hargaCleaned);


            int penumpangInt = Integer.parseInt(penumpang);

            double total = hargaDouble * penumpangInt;

            tvTotal.setText(String.valueOf(total));

            tvNama.setText(namaUser);
            tvPenumpang.setText(penumpang);
        }

        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TambahLokasiJemput.this, CurrentLocationActivity.class);
                startActivity(i);
            }
        });
    }
}