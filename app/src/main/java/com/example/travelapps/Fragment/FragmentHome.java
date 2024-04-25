package com.example.travelapps.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.travelapps.Adapter.InfoAdapter;
import com.example.travelapps.KotaActivity;
import com.example.travelapps.Model.Kota;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.TiketActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private InfoAdapter adapter;
    private List<Integer> itemList;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        itemList.add(R.drawable.banneratas);
        itemList.add(R.drawable.banner);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Spinner spinner;
    List<Kota> city = new ArrayList<>();
    String selectedId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        recyclerView = view.findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new InfoAdapter(getContext(), itemList);
//        recyclerView.setAdapter(adapter);
//        startAutoScroll();
        spinner = view.findViewById(R.id.spinner);
        ApiServices.showKota(getContext(), new ApiServices.KotaResponseListener() {
            @Override
            public void onSuccess(List<Kota> kotaList) {
                List<String> nama = new ArrayList<>();
                for (Kota kota : kotaList) {
                    nama.add(kota.getNama());
                }
                city.clear();
                city.addAll(kotaList);
                SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), city);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Kota selected = (Kota) parent.getItemAtPosition(position);
                        String selectedName = selected.getNama();

                        for (Kota kota : kotaList) {
                            if (kota.getNama().equals(selectedName)) {
                                selectedId = kota.getId();
                                break;
                            }
                        }
                        Log.e("Kota id" , selectedId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onError(String message) {
                Log.e("Error kota", message);
            }
        });
        Button pesanSekarangButton = view.findViewById(R.id.buttonPesanSekarang);
        pesanSekarangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tombol ditekan, buat Intent untuk memulai TiketActivity
                Intent intent = new Intent(getActivity(), TiketActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void startAutoScroll() {
        final int scrollSpeed = 10000; // Durasi antara setiap perpindahan item (dalam milidetik)
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int currentPosition = layoutManager.findFirstVisibleItemPosition();
                int nextPosition = currentPosition + 1;
                if (nextPosition >= adapter.getItemCount()) {
                    nextPosition = 0;
                }
                recyclerView.smoothScrollToPosition(nextPosition);
                handler.postDelayed(this, scrollSpeed);
            }
        };
        handler.postDelayed(runnable, scrollSpeed);
    }

    public static class SpinnerAdapter extends ArrayAdapter<Kota> {

        private LayoutInflater inflater;
        private List<Kota> kotaList;

        public SpinnerAdapter(Context context, List<Kota> kotaList) {
            super(context, 0, kotaList);
            this.kotaList = kotaList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_kota, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.nama_kota);
            textView.setText(kotaList.get(position).getNama());
            return convertView;
        }
    }

}