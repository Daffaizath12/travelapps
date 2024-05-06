package com.example.travelapps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.TiketSopir;
import com.example.travelapps.R;
import com.example.travelapps.TiketActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class PerjalananSopirAdapter extends RecyclerView.Adapter<PerjalananSopirAdapter.PerjalananSopirViewHolder>{
    private List<TiketSopir> tiketList;
    private Context context;
    private OnItemTiketSopirClickListener itemClickListener;

    public PerjalananSopirAdapter(Context context, List<TiketSopir> tiketList, OnItemTiketSopirClickListener itemClickListener) {
        this.context = context;
        this.tiketList = tiketList;
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public PerjalananSopirAdapter.PerjalananSopirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pemesanan_list_sopir, parent, false);
        return new PerjalananSopirViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerjalananSopirAdapter.PerjalananSopirViewHolder holder, int position) {
        TiketSopir tiket = tiketList.get(position);
        holder.bind(tiket);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    itemClickListener.onItemClick(tiket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiketList.size();
    }

    class PerjalananSopirViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, asalTextView, tujuanTextView, waktuTextView, hargaTextView, totalTextView;

        PerjalananSopirViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.tanggal);
            asalTextView = itemView.findViewById(R.id.tv_kota_asal);
            tujuanTextView = itemView.findViewById(R.id.tv_kota_tujuan);
            waktuTextView = itemView.findViewById(R.id.waktu);
            hargaTextView = itemView.findViewById(R.id.iv_harga);
            totalTextView = itemView.findViewById(R.id.iv_total);
        }

        void bind(TiketSopir tiket) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            String tanggalFormatted = sdf.format(tiket.getTanggal());

            dateTextView.setText(tanggalFormatted);
            asalTextView.setText(tiket.getAsal());
            tujuanTextView.setText(tiket.getTujuan());
            waktuTextView.setText(tiket.getWaktu());
            String hargaString = String.format("Rp %.2f", tiket.getHarga());
            hargaTextView.setText(hargaString);
            totalTextView.setText(tiket.getTotalPenumpang());
        }
    }

}
