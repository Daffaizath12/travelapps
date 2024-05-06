package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.User;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PesanActivity extends AppCompatActivity {

    TiketData tiketData;
    String id = "";
    String asal = "";
    String tujuan = "";
    Date tanggal;
    String waktu = "";
    String harga = "";
    String status = "";
    Double hargaDouble;
    String penumpang = "";
    String idUser = "";
    String namaUser = "";
    String emailUser = "";
    String alamatUser = "";
    String notelpUser = "";
    String jumlahPenumpang = "";
    String tanggalFormatted = "";

    TextView tvDate, tvAsal, tvWaktu, tvTujuan, tvNamaUser, tvEmailUser, tvNotelpUser, tvPenumpang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        SharedPreferences preferences = PesanActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        ImageButton backButton = findViewById(R.id.backtotiket);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ApiServices.getUserData(this, token, new ApiServices.UserResponseListener() {
            @Override
            public void onSuccess(User user) {
                idUser = user.getId();
                namaUser = user.getNama();
                notelpUser = user.getNotelp();
                emailUser = user.getEmail();
                alamatUser = user.getAlamat();

                tvNamaUser.setText(namaUser);
                tvNotelpUser.setText(notelpUser);
                tvEmailUser.setText(emailUser);
            }

            @Override
            public void onError(String message) {
                Log.e("Error", "Gagal mendapatkan data user");
            }
        });

        tvDate = findViewById(R.id.date);
        tvAsal = findViewById(R.id.asal);
        tvWaktu = findViewById(R.id.waktu);
        tvTujuan = findViewById(R.id.Tujuan);
        tvPenumpang = findViewById(R.id.txtPenumpang);
        tvNamaUser = findViewById(R.id.txtNama);
        tvEmailUser = findViewById(R.id.txtEmail);
        tvNotelpUser = findViewById(R.id.txtNoTelp);

        Intent intent = getIntent();
        if (intent.getExtras() != null){
            tiketData = (TiketData) intent.getSerializableExtra("tiket_data");
            id = tiketData.getId();
            asal = tiketData.getAsal();
            tujuan = tiketData.getTujuan();
            tanggal = tiketData.getTanggal();
            waktu = tiketData.getWaktu();
            jumlahPenumpang = tiketData.getJumlahPenumpang();
            hargaDouble = tiketData.getHarga();
            String hargaString = String.format("Rp %.2f", tiketData.getHarga());
            harga = hargaString;
            penumpang = intent.getStringExtra("penumpang");

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            tanggalFormatted = sdf.format(tanggal);

            tvDate.setText(tanggalFormatted);
            tvAsal.setText(asal);
            tvWaktu.setText(waktu);
            tvTujuan.setText(tujuan);
            tvPenumpang.setText(penumpang);
            AppCompatButton selanjutnyaButton = findViewById(R.id.selanjutnyapilihkursi);

            selanjutnyaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int jumlahPenumpangInt = Integer.parseInt(jumlahPenumpang);
                    int penumpangInt = Integer.parseInt(penumpang);

                    if (jumlahPenumpangInt < penumpangInt) {
                        Toast.makeText(PesanActivity.this, "Jumlah penumpang tidak mencukupi", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PesanActivity.this, PaymentMidtransActivity.class);
                        TiketData tiketData = new TiketData(id, asal, tujuan, tanggal, waktu, hargaDouble, jumlahPenumpang, status);
                        intent.putExtra("tiket", tiketData);
                        intent.putExtra("id_user", idUser);
                        intent.putExtra("nama_user", namaUser);
                        intent.putExtra("email_user", emailUser);
                        intent.putExtra("telp_user", notelpUser);
                        intent.putExtra("alamat_user", alamatUser);
                        intent.putExtra("penumpang", penumpang);
                        startActivity(intent);
                    }
                }
            });

        }

    }
}