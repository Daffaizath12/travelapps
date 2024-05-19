package com.example.travelapps.sopir.ui.notifications;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.travelapps.AboutActivity;
import com.example.travelapps.LoginActivity;
import com.example.travelapps.Model.Sopir;
import com.example.travelapps.ProfileActivity;
import com.example.travelapps.R;
import com.example.travelapps.databinding.FragmentNotifications2Binding;
import com.example.travelapps.databinding.FragmentSettingsBinding;
import com.example.travelapps.sopir.ApiServicesSopir;


public class NotificationsFragment extends Fragment {

    private FragmentNotifications2Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotifications2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayout about = binding.about;
        about.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), AboutActivity.class);
            startActivity(i);
        });
        LinearLayout logout = binding.logout;
        logout.setOnClickListener(
                view -> {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                    editor.remove("isLoginSopir");
                    editor.remove("id");
                    editor.apply();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                    startActivity(intent);
                    requireActivity().finish();
                 }
        );

        RelativeLayout relativeLayout = binding.profileSection;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("role", "sopir");
                startActivity(intent);
            }
        });
        SharedPreferences preferences = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("id", "");
        ApiServicesSopir.getSopirData(getContext(), token, new ApiServicesSopir.SopirResponseListener() {
            @Override
            public void onSuccess(Sopir sopir) {
                TextView tvNama = binding.userName;
                tvNama.setText(sopir.getNamaLengkap());
                TextView tvNickname = binding.profileImage;
                String twoInitials = sopir.getNamaLengkap().substring(0, 2);
                twoInitials = twoInitials.toUpperCase();
                tvNickname.setText(twoInitials);
            }

            @Override
            public void onError(String message) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}