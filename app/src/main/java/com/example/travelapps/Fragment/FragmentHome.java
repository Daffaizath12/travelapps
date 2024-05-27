package com.example.travelapps.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.travelapps.Adapter.InfoAdapter;
import com.example.travelapps.Adapter.KotaAdapter;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.TiketActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    EditText etPenumpang, etTanggal;
    Button btnPesan;
    private Spinner spinnerKotaAsal;
    private Spinner spinnerKotaTujuan;
    private KotaAdapter kotaAsalAdapter;
    private KotaAdapter kotaTujuanAdapter;

    String selectedKotaAsal = "";
    String formattedDate = "";
    String selectedKotaTujuan = "";
    private DatePickerDialog.OnDateSetListener mDate;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        etPenumpang = view.findViewById(R.id.etPenumpang);
        etTanggal = view.findViewById(R.id.et_tanggal);
        etPenumpang.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10")});
        spinnerKotaAsal = view.findViewById(R.id.spinner_asal);
        spinnerKotaTujuan = view.findViewById(R.id.spinner_tujuan);

        // Inisialisasi adapter dengan list kosong
        kotaAsalAdapter = new KotaAdapter(getContext(), new ArrayList<>());
        kotaTujuanAdapter = new KotaAdapter(getContext(), new ArrayList<>());
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());

                etTanggal.setText(formattedDate);
            }
        };
        // Set adapter ke spinner
        spinnerKotaAsal.setAdapter(kotaAsalAdapter);
        spinnerKotaTujuan.setAdapter(kotaTujuanAdapter);
        ApiServices.getCity(getContext(), new ApiServices.GetCityResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray kotaAsalArray = response.getJSONArray("kota_asal");
                    JSONArray kotaTujuanArray = response.getJSONArray("kota_tujuan");

                    List<String> kotaAsalList = new ArrayList<>();
                    List<String> kotaTujuanList = new ArrayList<>();

                    for (int i = 0; i < kotaAsalArray.length(); i++) {
                        kotaAsalList.add(kotaAsalArray.getString(i));
                    }

                    for (int i = 0; i < kotaTujuanArray.length(); i++) {
                        kotaTujuanList.add(kotaTujuanArray.getString(i));
                    }

                    // Update adapter dengan data baru
                    kotaAsalAdapter.clear();
                    kotaAsalAdapter.addAll(kotaAsalList);
                    kotaAsalAdapter.notifyDataSetChanged();

                    kotaTujuanAdapter.clear();
                    kotaTujuanAdapter.addAll(kotaTujuanList);
                    kotaTujuanAdapter.notifyDataSetChanged();

                    spinnerKotaAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedKotaAsal = (String) adapterView.getItemAtPosition(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Do nothing
                        }
                    });

                    spinnerKotaTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedKotaTujuan = (String) adapterView.getItemAtPosition(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Do nothing
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                // Handle error
            }
        });

        btnPesan = view.findViewById(R.id.buttonPesanSekarang);
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String penumpang = etPenumpang.getText().toString().trim();


                if (selectedKotaAsal.isEmpty()) {
                    Toast.makeText(getContext(), "Silahkan isi asal kota anda", Toast.LENGTH_SHORT).show();
                } else if (selectedKotaTujuan.isEmpty()){
                    Toast.makeText(getContext(), "Silahkan isi tujuan kota anda", Toast.LENGTH_SHORT).show();
                } else if (selectedKotaAsal.equals(selectedKotaTujuan)) {
                    Toast.makeText(getContext(), "Tujuan tidak boleh sama dengan asal", Toast.LENGTH_SHORT).show();
                } else if (penumpang.isEmpty()) {
                    etPenumpang.setError("Silakan isi jumlah penumpang");
                } else {
                    Intent intent = new Intent(getActivity(), TiketActivity.class);
                    intent.putExtra("asal", selectedKotaAsal);
                    intent.putExtra("tujuan", selectedKotaTujuan);
                    intent.putExtra("tanggal", formattedDate);
                    intent.putExtra("penumpang", penumpang);
                    startActivity(intent);
                }
            }
        });

        return view;
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