package com.example.travelapps.sopir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.travelapps.Adapter.PenumpangAdapter;
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.TiketSopir;
import com.example.travelapps.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class DetailSopirActivity extends AppCompatActivity {

    String idSopir = "";
    String idPerjalanan = "";
    RecyclerView recyclerView;
    private List<PemesananSopir> pemesananSopirList;
    private PenumpangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sopir);
        RecyclerView recyclerView = findViewById(R.id.rv_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pemesananSopirList = new ArrayList<>();
        adapter = new PenumpangAdapter(this, pemesananSopirList);
        recyclerView.setAdapter(adapter);
        Intent i = getIntent();
        if (i.getExtras() != null) {
            TiketSopir tiketData = (TiketSopir) i.getSerializableExtra("tiket");
            idPerjalanan = tiketData.getId();
            idSopir = i.getStringExtra("id");
        }

        ApiServicesSopir.getPenumpangSopirActive(this, idSopir, idPerjalanan, new ApiServicesSopir.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<PemesananSopir> pemesananSopir) {
                pemesananSopirList.clear();
                pemesananSopirList.addAll(pemesananSopir);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
//                Toast.makeText(DetailSopirActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                Log.e("detail" , "Error: " + message);
            }
        });
    }
}