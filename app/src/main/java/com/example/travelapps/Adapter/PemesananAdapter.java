package com.example.travelapps.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.Pemesanan;
import com.example.travelapps.R;

import java.util.List;

public class PemesananAdapter extends RecyclerView.Adapter<PemesananAdapter.PemesananViewHolder> {

    private List<Pemesanan.PemesananData> pemesananList;

    public PemesananAdapter(List<Pemesanan.PemesananData> pemesananList) {
        this.pemesananList = pemesananList;
    }

    @NonNull
    @Override
    public PemesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pemesanan_list, parent, false);
        return new PemesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PemesananViewHolder holder, int position) {
        Pemesanan.PemesananData pemesanan = pemesananList.get(position);
        holder.bind(pemesanan);
    }

    @Override
    public int getItemCount() {
        return pemesananList.size();
    }

    public class PemesananViewHolder extends RecyclerView.ViewHolder {

        private TextView orderIdTextView;
        private TextView waktuTextView;
        private TextView tanggalBerangkatTextView;
        private TextView asalTextView;
        private TextView tujuanTextView;

        public PemesananViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            tanggalBerangkatTextView = itemView.findViewById(R.id.date);
            asalTextView = itemView.findViewById(R.id.asal);
            waktuTextView = itemView.findViewById(R.id.waktu);
            tujuanTextView = itemView.findViewById(R.id.Tujuan);
        }

        public void bind(Pemesanan.PemesananData pemesananData) {
            orderIdTextView.setText(pemesananData.getOrderId());
            waktuTextView.setText(pemesananData.getWaktuKeberangkatan());
            tanggalBerangkatTextView.setText(pemesananData.getTanggalBerangkat());
            asalTextView.setText(pemesananData.getKotaAsal());
            tujuanTextView.setText(pemesananData.getKotaTujuan());
        }
    }
}
