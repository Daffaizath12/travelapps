package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.travelapps.Adapter.TiketAdapter;
import com.example.travelapps.Model.TiketData;

import java.util.ArrayList;
import java.util.List;

public class TiketActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TiketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for demonstration
        List<TiketData> tiketList = new ArrayList<>();
        tiketList.add(new TiketData("Monday, 12 Agustus", "Banyuwangi", "Malang", "21.00 WIB", "150.000"));
        tiketList.add(new TiketData("Monday, 12 Agustus", "Malang", "Banyuwangi", "21.00 WIB", "170.000"));
        tiketList.add(new TiketData("Monday, 12 Agustus", "Banyuwangi", "Surabaya", "21.00 WIB", "180.000"));
        tiketList.add(new TiketData("Monday, 12 Agustus", "Surabaya", "Banyuwangi", "21.00 WIB", "200.000"));
        // Add more data as needed

        adapter = new TiketAdapter(this, tiketList);
        recyclerView.setAdapter(adapter);
    }
}
