package com.example.travelapps.sopir.ui.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapps.Adapter.OnItemTiketSopirClickListener;
import com.example.travelapps.Adapter.PerjalananSopirAdapter;
import com.example.travelapps.Adapter.TiketAdapter;
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.TiketSopir;
import com.example.travelapps.databinding.FragmentDashboardBinding;
import com.example.travelapps.sopir.ApiServicesSopir;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnItemTiketSopirClickListener {

    private FragmentDashboardBinding binding;
    private List<TiketSopir> tiketDataList;
    private PerjalananSopirAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.rvDaftar;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tiketDataList =  new ArrayList<>();
        adapter = new PerjalananSopirAdapter(getContext(), tiketDataList, this);
        recyclerView.setAdapter(adapter);
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String idSopir = preferences.getString("id", "");
        getPerjalananSopirFromApi(idSopir);
        return root;
    }

    private void getPerjalananSopirFromApi(String idSopir) {
        ApiServicesSopir.getTiketSopir(getContext(), idSopir, new ApiServicesSopir.TiketSopirResponseListener() {
            @Override
            public void onSuccess(List<TiketSopir> pemesananSopir) {
                tiketDataList.clear();
                tiketDataList.addAll(pemesananSopir);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("DashboardFragmentSopir", "Error: " + message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(TiketSopir tiketData) {
    }
}