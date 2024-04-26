package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.travelapps.Adapter.TiketAdapter;
import com.example.travelapps.Model.Perjalanan;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Services.ApiServices;

import java.util.ArrayList;
import java.util.List;

public class TiketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TiketAdapter adapter;
    String kotaAsal;
    String kotaTujuan;
    String penumpang;
    Perjalanan perjalanan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
           kotaAsal = intent.getStringExtra("asal");
           kotaTujuan = intent.getStringExtra("tujuan");
           penumpang = intent.getStringExtra("penumpang");
        }
        
        ApiServices.showPerjalanan(this, kotaAsal, kotaTujuan, new ApiServices.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<TiketData> tiketData) {
                adapter = new TiketAdapter(TiketActivity.this, tiketData);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                Log.e("Gagal mendapatkan data", message);
            }
        });

    }
}
