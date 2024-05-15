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

import com.example.travelapps.Adapter.NotifikasiAdapter;
import com.example.travelapps.Model.Notifikasi;
import com.example.travelapps.Model.User;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNotifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNotifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotifications.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotifications newInstance(String param1, String param2) {
        FragmentNotifications fragment = new FragmentNotifications();
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
    NotifikasiAdapter notifikasiAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notifications, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rv_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        ApiServices.getUserData(getContext(), token, new ApiServices.UserResponseListener() {
            @Override
            public void onSuccess(User user) {
                String idUser = user.getId();
                ApiServices.getNotifikasi(getContext(), idUser, new ApiServices.NotifikasiResponseListener() {
                    @Override
                    public void onSuccess(List<Notifikasi> notifikasiList) {
                        notifikasiAdapter = new NotifikasiAdapter(getContext(), notifikasiList);
                        recyclerView.setAdapter(notifikasiAdapter);
                    }

                    @Override
                    public void onError(String message) {
                        Log.e("notifications" , message);
                    }
                });
            }

            @Override
            public void onError(String message) {

            }
        });
        return v;
    }
}