package com.example.travelapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.TiketData;
import com.example.travelapps.PesanActivity;
import com.example.travelapps.R;
import com.example.travelapps.TiketActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TiketAdapter extends RecyclerView.Adapter<TiketAdapter.TiketViewHolder> {
    private List<TiketData> tiketList;
    private Context context;

    public TiketAdapter(Context context, List<TiketData> tiketList) {
        this.context = context;
        this.tiketList = tiketList;
    }

    @NonNull
    @Override
    public TiketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tiket, parent, false);
        return new TiketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TiketViewHolder holder, int position) {
        TiketData tiket = tiketList.get(position);
        holder.bind(tiket);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof TiketActivity) {
                    String penumpang = ((TiketActivity) context).getPenumpang();
                    Intent intent = new Intent(context, PesanActivity.class);
                    intent.putExtra("tiket_data", tiket);
                    intent.putExtra("penumpang", penumpang);
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return tiketList.size();
    }

    class TiketViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, asalTextView, tujuanTextView, waktuTextView, hargaTextView, statusTextView;

        TiketViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            asalTextView = itemView.findViewById(R.id.asal);
            tujuanTextView = itemView.findViewById(R.id.Tujuan);
            waktuTextView = itemView.findViewById(R.id.waktu);
            hargaTextView = itemView.findViewById(R.id.harga);
            statusTextView = itemView.findViewById(R.id.status);
        }

        void bind(TiketData tiket) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            // Mengonversi objek Date menjadi string tanggal dengan format yang diinginkan
            String tanggalFormatted = sdf.format(tiket.getTanggal());

            // Mengatur teks untuk setiap TextView dengan data dari objek TiketData
            dateTextView.setText(tanggalFormatted);
            asalTextView.setText(tiket.getAsal());
            tujuanTextView.setText(tiket.getTujuan());
            waktuTextView.setText(tiket.getWaktu());
            String hargaString = String.format("Rp %.2f", tiket.getHarga());
            hargaTextView.setText(hargaString);
            statusTextView.setText(tiket.getStatus());
        }
    }
}

