package com.example.travelapps.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.R;

public class NotifikasiViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate, tvTitle, tvDesc;

    public NotifikasiViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvDesc = itemView.findViewById(R.id.tv_desc);
    }
}
