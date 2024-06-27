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

public class PenumpangAdapterActive extends RecyclerView.Adapter<PenumpangAdapterActive.PenumpangViewHolder> {

    private List<PemesananSopir> penumpangList;
    private Context context;
    private OnStatusUpdateListener onStatusUpdateListener;
    private boolean isDestinationLocation = false;

    public PenumpangAdapterActive(Context context, List<PemesananSopir> penumpangList, OnStatusUpdateListener onStatusUpdateListener) {
        this.context = context;
        this.penumpangList = penumpangList;
        this.onStatusUpdateListener = onStatusUpdateListener;

    }

    @NonNull
    @Override
    public PenumpangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.penumpang_list, parent, false);
        return new PenumpangViewHolder(view);
    }

    public void setDestinationLocation(boolean isDestinationLocation) {
        this.isDestinationLocation = isDestinationLocation;
    }

    @Override
    public void onBindViewHolder(@NonNull PenumpangViewHolder holder, int position) {
        PemesananSopir penumpang = penumpangList.get(position);
        holder.txtName.setText(penumpang.getNamaLengkap());
        holder.txtAlamat.setText(penumpang.getAlamatJemput());
        holder.txtJumlah.setText(penumpang.getQty());
        holder.txtTelp.setText(penumpang.getNotelp());
        if (isDestinationLocation) {
            holder.txtAlamat.setText(penumpang.getAlamatTujuan());
            holder.txtUrutan.setVisibility(View.GONE);
            if (penumpang.getAntar().equalsIgnoreCase("active")) {
                holder.btnSelesai.setVisibility(View.VISIBLE);
                holder.btnSelesai.setText("antar");
                holder.btnSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiServicesSopir.tujuanStatus(context, penumpang.getIdPemesanan(), new ApiServicesSopir.UpdateStatusResponseListener() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(context, "Berhasil update status pengantaran", Toast.LENGTH_SHORT).show();
                                if (onStatusUpdateListener != null) {
                                    onStatusUpdateListener.onStatusUpdated();
                                }
                            }

                            @Override
                            public void onError(String message) {
                                Log.e("update-status" , message);
                            }
                        });
                    }
                });
            }else {
                holder.btnSelesai.setVisibility(View.GONE);
            }

        } else {
            holder.txtUrutan.setVisibility(View.VISIBLE);
            holder.txtUrutan.setText("Penjemputan ke-" + (position + 1));
            holder.btnSelesai.setVisibility(View.VISIBLE);
            holder.btnSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApiServicesSopir.updateStatus(context, penumpang.getIdPemesanan(), new ApiServicesSopir.UpdateStatusResponseListener() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(context, "Berhasil update status penjemputan", Toast.LENGTH_SHORT).show();
                            if (onStatusUpdateListener != null) {
                                onStatusUpdateListener.onStatusUpdated();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Log.e("update-status" , message);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return penumpangList.size();
    }
    public interface OnStatusUpdateListener {
        void onStatusUpdated();
    }
    public class PenumpangViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAlamat, txtJumlah, txtTelp, txtUrutan;
        AppCompatButton btnSelesai;

        public PenumpangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name);
            txtJumlah = itemView.findViewById(R.id.jumlah);
            txtAlamat = itemView.findViewById(R.id.address);
            txtUrutan = itemView.findViewById(R.id.urutan);
            txtTelp = itemView.findViewById(R.id.telp);
            btnSelesai = itemView.findViewById(R.id.btn_selesai);
        }
    }
}


