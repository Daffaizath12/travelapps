package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.Pemesanan;
import com.example.travelapps.Model.TransactionModel;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.Services.MidtransServices;

import java.util.Objects;

public class DetailPemesananActivity extends AppCompatActivity {

    TextView tvAsal, tvTujuan, tvDate, tvWaktu, tvPenumpang, tvStatus, tvJemput, tvATujuan;
    AppCompatButton btnBayar;
    Pemesanan.PemesananData pemesanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);
        onBindView();
        getIntentView();
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailPemesananActivity.this, "hahah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBindView(){
        tvAsal = findViewById(R.id.asal);
        tvTujuan = findViewById(R.id.Tujuan);
        tvDate = findViewById(R.id.date);
        tvWaktu = findViewById(R.id.waktu);
        tvPenumpang = findViewById(R.id.txtPenumpang);
        tvStatus = findViewById(R.id.tvStatus);
        tvJemput = findViewById(R.id.tvAlamatJemput);
        tvATujuan = findViewById(R.id.tvAlamatTujuan);
        btnBayar = findViewById(R.id.bayarsekarang);
//        btnBayar.setVisibility(View.GONE);
    }

    public void getIntentView(){
        Intent i = getIntent();
        if (i.getExtras() != null) {
            pemesanan = (Pemesanan.PemesananData) i.getSerializableExtra("pemesananData");

            String asal = pemesanan.getKotaAsal();
            String tujuan = pemesanan.getKotaTujuan();
            String tanggal = pemesanan.getTanggal();
            String waktu = pemesanan.getWaktuKeberangkatan();
            String jemput = pemesanan.getAlamatJemput();
            String tujuanA = pemesanan.getAlamatTujuan();
            String orderId = pemesanan.getOrderId();
            String qty = pemesanan.getQty();
            String status1 = pemesanan.getStatus();

            tvAsal.setText(asal);
            tvTujuan.setText(tujuan);
            tvDate.setText(tanggal);
            tvWaktu.setText(waktu);
            tvJemput.setText(jemput);
            tvATujuan.setText(tujuanA);
            tvPenumpang.setText(qty);
            tvStatus.setText(status1);

            if (!Objects.equals(status1, "Gagal")) {
                MidtransServices midtransServices = new MidtransServices(this);
                midtransServices.getTransactionStatus(orderId, new MidtransServices.TransactionStatusResponseListener() {
                    @Override
                    public void onSuccess(TransactionModel transactionModel) {
                        String status = transactionModel.getTransactionStatus();
                        String orderId = transactionModel.getOrderId();
                        String status1 = pemesanan.getStatus();

                        if (Objects.equals(status, "failure")) {
                            ApiServices.status(DetailPemesananActivity.this, orderId, "Gagal", new ApiServices.AddLatlongResponseListener() {
                                @Override
                                public void onSuccess(String message) {
                                    Log.e("statusPesan", "berhasil update status gagal");
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("statusPesan", message);
                                }
                            });
                        } else if(Objects.equals(status, "pending")){
                            ApiServices.status(DetailPemesananActivity.this, orderId, "Menunggu", new ApiServices.AddLatlongResponseListener() {
                                @Override
                                public void onSuccess(String message) {
                                    Log.e("statusPesan", "berhasil update status menunggu");
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("statusPesan", message);
                                }
                            });
                        } else if(Objects.equals(status, "settlement")){
                            ApiServices.status(DetailPemesananActivity.this, orderId, "Selesai", new ApiServices.AddLatlongResponseListener() {
                                @Override
                                public void onSuccess(String message) {
                                    Log.e("statusPesan", "berhasil update status berhasil");
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("statusPesan", message);
                                }
                            });
                        } else {
                            Log.e("statusPesan", status);

                        }
                        tvStatus.setText(status1);

                    }
                    @Override
                    public void onError(String message) {
                        Log.e("DetailPemesananActivity", "Error: " + message);
                    }
                });

            } else if(Objects.equals(status1, "Menunggu")){
                btnBayar.setVisibility(View.VISIBLE);
//                btnBayar.setEnabled(true);
//                btnBayar.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color_30));

            } else {
                btnBayar.setVisibility(View.GONE);
            }

        }
    }
}