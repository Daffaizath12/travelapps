package com.example.travelapps.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.Kota;
import com.example.travelapps.R;

import java.util.List;

public class KotaAdapter extends RecyclerView.Adapter<KotaAdapter.KotaViewHolder> {

    private Context mContext;
    private List<Kota> mKotaList;
    private String kotaTerpilih;

    public KotaAdapter(Context context, List<Kota> kotaList) {
        mContext = context;
        mKotaList = kotaList;
        kotaTerpilih = "";
    }

    @NonNull
    @Override
    public KotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kota, parent, false);
        return new KotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KotaViewHolder holder, int position) {
        Kota kota = mKotaList.get(position);
        holder.namaKota.setText(kota.getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kotaTerpilih = mKotaList.get(holder.getAdapterPosition()).getNama();
                Log.e("kotaterpilih", kotaTerpilih);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mKotaList.size();
    }

    public String getKotaTerpilih() {
        return kotaTerpilih;
    }

    public void updateData(List<Kota> newData) {
        mKotaList.clear();
        mKotaList.addAll(newData);
        notifyDataSetChanged();
    }
    public static class KotaViewHolder extends RecyclerView.ViewHolder {
        TextView namaKota;

        public KotaViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKota = itemView.findViewById(R.id.nama_kota);
        }
    }
}
