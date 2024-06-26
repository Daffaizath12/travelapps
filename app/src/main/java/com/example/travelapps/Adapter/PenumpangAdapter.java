package com.example.travelapps.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.R;
import com.example.travelapps.sopir.ApiServicesSopir;

import java.util.List;

public class PenumpangAdapter extends RecyclerView.Adapter<PenumpangAdapter.PenumpangViewHolder> {

    private List<PemesananSopir> penumpangList;
    private Context context;

    public PenumpangAdapter(Context context, List<PemesananSopir> penumpangList) {
        this.context = context;
        this.penumpangList = penumpangList;
    }

    @NonNull
    @Override
    public PenumpangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.penumpang_list_active, parent, false);
        return new PenumpangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenumpangViewHolder holder, int position) {
        PemesananSopir penumpang = penumpangList.get(position);
        holder.txtName.setText(penumpang.getNamaLengkap());
        holder.txtAlamat.setText(penumpang.getAlamatJemput());
        holder.txtJumlah.setText(penumpang.getQty());
        holder.txtTelp.setText(penumpang.getNotelp());
    }

    @Override
    public int getItemCount() {
        return penumpangList.size();
    }

    public class PenumpangViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAlamat, txtJumlah, txtTelp;

        public PenumpangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name);
            txtJumlah = itemView.findViewById(R.id.jumlah);
            txtAlamat = itemView.findViewById(R.id.address);
            txtTelp = itemView.findViewById(R.id.telp);
        }
    }
}


