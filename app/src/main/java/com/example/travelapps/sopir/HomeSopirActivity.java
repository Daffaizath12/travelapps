package com.example.travelapps.sopir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.travelapps.Fragment.FragmentActivity;
import com.example.travelapps.Fragment.FragmentHome;
import com.example.travelapps.Fragment.FragmentNotifications;
import com.example.travelapps.Fragment.FragmentSettings;
import com.example.travelapps.R;
import com.example.travelapps.sopir.ui.dashboard.DashboardFragment;
import com.example.travelapps.sopir.ui.home.HomeFragment;
import com.example.travelapps.sopir.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeSopirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sopir);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Tampilkan fragment pertama kali (misalnya, FragmentHome)
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home_sopir,
                new HomeFragment()).commit();
    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.aktivitas:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment = new FragmentSettings();
                            break;
                    }

                    // Ganti fragment yang ditampilkan
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_home_sopir,
                            selectedFragment).commit();

                    return true;
                }
            };
}