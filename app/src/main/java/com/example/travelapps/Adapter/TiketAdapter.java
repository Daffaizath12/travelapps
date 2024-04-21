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
    }

    @Override
    public int getItemCount() {
        return tiketList.size();
    }

    class TiketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateTextView, asalTextView, tujuanTextView, waktuTextView, hargaTextView;

        TiketViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dateTextView = itemView.findViewById(R.id.date);
            asalTextView = itemView.findViewById(R.id.asal);
            tujuanTextView = itemView.findViewById(R.id.Tujuan);
            waktuTextView = itemView.findViewById(R.id.waktu);
            hargaTextView = itemView.findViewById(R.id.harga);
        }

        void bind(TiketData tiket) {
            dateTextView.setText(tiket.getDate());
            asalTextView.setText(tiket.getAsal());
            tujuanTextView.setText(tiket.getTujuan());
            waktuTextView.setText(tiket.getWaktu());
            hargaTextView.setText(tiket.getHarga());
        }

        @Override
        public void onClick(View v) {
            // Ketika card diklik, buat Intent untuk membuka PesanActivity
            Intent intent = new Intent(context, PesanActivity.class);
            context.startActivity(intent);
        }
    }
}

