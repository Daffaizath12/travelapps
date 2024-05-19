package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapps.Adapter.OnItemTiketClickListener;
import com.example.travelapps.Adapter.TiketAdapter;
import com.example.travelapps.Model.Perjalanan;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.R;

import java.util.ArrayList;
import java.util.List;

public class TiketActivity extends AppCompatActivity implements OnItemTiketClickListener {
    private RecyclerView recyclerView;
    private TiketAdapter adapter;
    String kotaAsal;
    String kotaTujuan;
    String penumpang;
    Perjalanan perjalanan;
    TextView tvNull;
    ImageView ivBack;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvNull = findViewById(R.id.tvNull);
        ivBack = findViewById(R.id.backtohome);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
           kotaAsal = intent.getStringExtra("asal");
           kotaTujuan = intent.getStringExtra("tujuan");
           penumpang = intent.getStringExtra("penumpang");

           Log.e("intent", kotaAsal + kotaTujuan + penumpang);
        }

        ApiServices.showPerjalanan(this, kotaAsal, kotaTujuan, new ApiServices.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<TiketData> tiketData) {
                if (tiketData.isEmpty()) {
                    tvNull.setText("Tidak ada tiket perjalanan");
                }
                adapter = new TiketAdapter(TiketActivity.this, tiketData, TiketActivity.this, penumpang);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                Log.e("Gagal mendapatkan data", message);
            }
        });
    }
    public String getPenumpang() {
        return penumpang;
    }

    @Override
    public void onItemClick(TiketData tiketData) {
                    Intent intent = new Intent(TiketActivity.this, PesanActivity.class);
                    intent.putExtra("tiket_data", tiketData);
                    intent.putExtra("penumpang", penumpang);
                    startActivity(intent);
    }
}
