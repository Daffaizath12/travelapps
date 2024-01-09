package com.example.travelapps;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.travelapps.Fragment.FragmentActivity;
import com.example.travelapps.Fragment.FragmentHome;
import com.example.travelapps.Fragment.FragmentNotifications;
import com.example.travelapps.Fragment.FragmentSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Tampilkan fragment pertama kali (misalnya, FragmentHome)
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer,
                new FragmentHome()).commit();
    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.menu_profile:
                            selectedFragment = new FragmentActivity();
                            break;
                        case R.id.menu_notifications:
                            selectedFragment = new FragmentNotifications();
                            break;
                        case R.id.menu_settings:
                            selectedFragment = new FragmentSettings();
                            break;
                    }

                    // Ganti fragment yang ditampilkan
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer,
                            selectedFragment).commit();

                    return true;
                }
            };
}



