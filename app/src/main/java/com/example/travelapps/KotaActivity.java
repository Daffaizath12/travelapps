package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.travelapps.Adapter.KotaAdapter;
import com.example.travelapps.Model.Kota;
import com.example.travelapps.Services.ApiServices;

import java.util.ArrayList;
import java.util.List;

public class KotaActivity extends AppCompatActivity {

    RecyclerView rvKota;
    KotaAdapter kotaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kota);

        rvKota = findViewById(R.id.rvKota);
        rvKota.setLayoutManager(new LinearLayoutManager(this));

        kotaAdapter = new KotaAdapter(this, new ArrayList<>());
        rvKota.setAdapter(kotaAdapter);

        getKotaData();
    }

    private void getKotaData() {
        ApiServices.showKota(this, new ApiServices.KotaResponseListener() {
            @Override
            public void onSuccess(List<Kota> kotaList) {
                kotaAdapter.updateData(kotaList);
                Log.e("kota" , kotaList.toString());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(KotaActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String kotaTerpilih = kotaAdapter.getKotaTerpilih();
        Intent intent = new Intent();
        intent.putExtra("kota_terpilih", kotaTerpilih);
        setResult(RESULT_OK, intent);
        finish();
    }

}