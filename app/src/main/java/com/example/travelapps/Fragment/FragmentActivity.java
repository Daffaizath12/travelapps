package com.example.travelapps.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travelapps.Adapter.PemesananAdapter;
import com.example.travelapps.Model.Pemesanan;
import com.example.travelapps.Model.User;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentActivity newInstance(String param1, String param2) {
        FragmentActivity fragment = new FragmentActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView recyclerView;
    PemesananAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        ApiServices.getUserData(getContext(), token, new ApiServices.UserResponseListener() {
            @Override
            public void onSuccess(User user) {
                String idUser = user.getId();
                ApiServices.showPemesanan(getContext(), idUser, new ApiServices.ShowPemesananResponseListener() {
                    @Override
                    public void onSuccess(List<Pemesanan.PemesananData> pemesananDataList) {
                        adapter = new PemesananAdapter(getContext(), pemesananDataList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                Log.e("Error", "Gagal mendapatkan data user");
            }
        });

        return view;
    }
}