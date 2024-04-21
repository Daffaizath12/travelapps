package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PesanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        ImageButton backButton = findViewById(R.id.backtotiket);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode untuk kembali ke aktivitas sebelumnya
                onBackPressed();
            }
        });

        AppCompatButton selanjutnyaButton = findViewById(R.id.selanjutnyapilihkursi);
        selanjutnyaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat dan memulai intent ke PilihKursiActivity
                Intent intent = new Intent(PesanActivity.this, TambahLokasiJemput.class);
                startActivity(intent);
            }
        });


    }
}