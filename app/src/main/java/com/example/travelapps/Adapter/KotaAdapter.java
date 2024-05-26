package com.example.travelapps.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.Kota;
import com.example.travelapps.R;

import java.util.List;

public class KotaAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> kotaList;

    public KotaAdapter(@NonNull Context context, @NonNull List<String> kotaList) {
        super(context, 0, kotaList);
        this.context = context;
        this.kotaList = kotaList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spiner_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.names);
        String currentKota = getItem(position);

        if (currentKota != null) {
            textViewName.setText(currentKota);
        }

        return convertView;
    }
}
