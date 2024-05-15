package com.example.travelapps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelapps.Model.Notifikasi;
import com.example.travelapps.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiViewHolder> {
    private List<Notifikasi> notifikasiList;
    private Context context;

    public NotifikasiAdapter(Context context, List<Notifikasi> notifikasiList) {
        this.context = context;
        this.notifikasiList = notifikasiList;
    }

    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_list, parent, false);
        return new NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int position) {
        Notifikasi notifikasi = notifikasiList.get(position);
        holder.tvTitle.setText(notifikasi.getTitle());
        holder.tvDesc.setText(notifikasi.getDesc());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        holder.tvDate.setText(dateFormat.format(notifikasi.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return notifikasiList.size();
    }
}

