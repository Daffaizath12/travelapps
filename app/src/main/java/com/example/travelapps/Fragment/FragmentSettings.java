package com.example.travelapps.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.travelapps.LoginActivity;
import com.example.travelapps.Model.User;
import com.example.travelapps.ProfileActivity;
import com.example.travelapps.R;
import com.example.travelapps.Services.ApiServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSettings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSettings newInstance(String param1, String param2) {
        FragmentSettings fragment = new FragmentSettings();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        RelativeLayout relativeLayout = view.findViewById(R.id.profileSection);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                editor.remove("isLogin");
                editor.remove("token");
                editor.apply(); // Simpan perubahan pada SharedPreferences
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                requireActivity().finish();
            }
        });
        ApiServices.getUserData(getContext(), token, new ApiServices.UserResponseListener() {
            @Override
            public void onSuccess(User user) {
                TextView tvname = view.findViewById(R.id.userName);
                tvname.setText(user.getNama());
                TextView tvNickname = view.findViewById(R.id.profileImage);
                String twoInitials = user.getNama().substring(0, 2);
                twoInitials = twoInitials.toUpperCase();
                tvNickname.setText(twoInitials);
            }

            @Override
            public void onError(String message) {

            }
        });
        return view;
    }
}
