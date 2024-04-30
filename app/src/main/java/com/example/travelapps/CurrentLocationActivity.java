package com.example.travelapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Services.ApiServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMaps;
    final private int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    TextView tvAlamat;
    AppCompatButton btnSimpan;
    FusedLocationProviderClient fusedLocationProviderClient;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        SharedPreferences preferences = CurrentLocationActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        tvAlamat = findViewById(R.id.alamat);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.addLatlong(CurrentLocationActivity.this, token, currentLocation.getLatitude(), currentLocation.getLongitude(), new ApiServices.AddLatlongResponseListener() {
                    @Override
                    public void onSuccess(String message) {
                        onBackPressed();
                        Toast.makeText(CurrentLocationActivity.this, "Berhasil menyimpan lokasi penjemputan", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(CurrentLocationActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(CurrentLocationActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMaps = googleMap;

        LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMaps.addMarker(new MarkerOptions().position(sydney).title("MyLocation"));
        float zoomLevel = 16.0f;
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
        getAddressFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location Permission is Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                tvAlamat.setText(addressLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}