package com.example.travelapps.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.travelapps.Adapter.InfoAdapter;
import com.example.travelapps.R;
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

    EditText etPenumpang, etAsal, etTujuan;
    Button btnPesan;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        recyclerView = view.findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new InfoAdapter(getContext(), itemList);
//        recyclerView.setAdapter(adapter);
//        startAutoScroll();
        etAsal = view.findViewById(R.id.etAsal);
        etTujuan = view.findViewById(R.id.etTujuan);
        etPenumpang = view.findViewById(R.id.etPenumpang);
        etPenumpang.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});

        btnPesan = view.findViewById(R.id.buttonPesanSekarang);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asal = etAsal.getText().toString().trim();
                String tujuan = etTujuan.getText().toString().trim();
                String penumpang = etPenumpang.getText().toString().trim();

                if (asal.isEmpty()) {
                    etAsal.setError("Silahkan isi asal kota anda");
                } else if (tujuan.isEmpty()){
                    etTujuan.setError("Silakan isi tujuan kota anda");
                } else if (asal.equals(tujuan)) {
                    etTujuan.setError("Tujuan tidak boleh sama dengan asal");
                } else if (penumpang.isEmpty()) {
                    etPenumpang.setError("Silakan isi jumlah penumpang");
                } else {
                    Intent intent = new Intent(getActivity(), TiketActivity.class);
                    intent.putExtra("asal", asal);
                    intent.putExtra("tujuan", tujuan);
                    intent.putExtra("penumpang", penumpang);
                    startActivity(intent);
                }
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

    public class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input)) {
                    return null;
                }
            } catch (NumberFormatException ignored) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}