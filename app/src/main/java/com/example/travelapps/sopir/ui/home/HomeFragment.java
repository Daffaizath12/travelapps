package com.example.travelapps.sopir.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Adapter.OnItemTiketClickListener;
import com.example.travelapps.Adapter.TiketAdapter;
import com.example.travelapps.Model.Sopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.PesanActivity;
import com.example.travelapps.databinding.FragmentHome2Binding;
import com.example.travelapps.sopir.ApiServicesSopir;
import com.example.travelapps.sopir.MapsSopirActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemTiketClickListener {

    private FragmentHome2Binding binding;
    private List<TiketData> tiketDataList;
    private TiketAdapter adapter;
    String idSopir = "";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHome2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.rvPerjalanan;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tiketDataList = new ArrayList<>();
        adapter = new TiketAdapter(getContext(), tiketDataList, this);
        recyclerView.setAdapter(adapter);
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        idSopir = preferences.getString("id", "");
        getPerjalananSopirFromApi(idSopir);
        ApiServicesSopir.getSopirData(getContext(), idSopir, new ApiServicesSopir.SopirResponseListener() {
            @Override
            public void onSuccess(Sopir sopir) {
                TextView tvNama = binding.tvNama;
                tvNama.setText(sopir.getNamaLengkap());
                TextView tvSim = binding.tvSim;
                tvSim.setText(sopir.getNoSim());
            }

            @Override
            public void onError(String message) {

            }
        });
        return root;
    }

    private void getPerjalananSopirFromApi(String idSopir) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateStr = sdf.format(currentDate);
        ApiServicesSopir.getPerjalananSopirNow(getContext(), idSopir, currentDateStr, new ApiServicesSopir.PerjalananNowResponseListener() {
            @Override
            public void onSuccess(List<TiketData> tiketDataListResponse) {
                tiketDataList.clear();
                tiketDataList.addAll(tiketDataListResponse);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                Log.e("HomeFragmentSopir", "Error: " + message);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(TiketData tiketData) {
        Intent i = new Intent(getContext(), MapsSopirActivity.class);
        i.putExtra("id", idSopir);
        i.putExtra("tiket", tiketData);
        startActivity(i);
    }
}