package com.example.travelapps.sopir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsSopirActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap gMaps;
    final private int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    String idSopir = "";
    String idPerjalanan = "";
    RecyclerView recyclerView;
    private ProgressDialog dialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng userLocation, destinationLocation;
    Polyline currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_sopir);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        Intent i = getIntent();
        if (i.getExtras() != null) {
            TiketData tiketData = (TiketData) i.getSerializableExtra("tiket");
            idPerjalanan = tiketData.getId();
            idSopir = i.getStringExtra("id");
        }

        dialog = new ProgressDialog(MapsSopirActivity.this);
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
                    userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsSopirActivity.this);
                }
            }
        });
    }

    private void locationUser() {
        ApiServicesSopir.getPenumpangSopir(MapsSopirActivity.this, idSopir, idPerjalanan, new ApiServicesSopir.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<PemesananSopir> pemesananSopir) {
                List<LatLng> coordinates = new ArrayList<>();
                for (PemesananSopir pemesanan : pemesananSopir) {
                    double latitude = Double.parseDouble(pemesanan.getLatitude());
                    double longitude = Double.parseDouble(pemesanan.getLongitude());
                    LatLng userLatLng = new LatLng(latitude, longitude);
                    coordinates.add(userLatLng);
                    String username = pemesanan.getNamaLengkap();
                    gMaps.addMarker(new MarkerOptions().position(userLatLng).title(username));
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MapsSopirActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMaps = googleMap;
        gMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (currentRoute != null) {
                    currentRoute.remove();
                }
                dialog.setMessage("Rute sedang dimuat , mohon tunggu sebentar");
                dialog.show();
                LatLng destinationLatLng = marker.getPosition();
                getRouteFromOSRM(userLocation, destinationLatLng);
                return false;
            }
        });
        locationUser();
        LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMaps.addMarker(new MarkerOptions().position(sydney).title("Lokasi Anda"));
        float zoomLevel = 14.0f;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
        gMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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


    private void getRouteFromOSRM(LatLng origin, LatLng destination) {
        String url = "http://router.project-osrm.org/route/v1/driving/" +
                origin.longitude + "," + origin.latitude + ";" +
                destination.longitude + "," + destination.latitude +
                "?steps=true&annotations=true&overview=full&geometries=geojson";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleGeoJSONResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }
    private void handleGeoJSONResponse(JSONObject response) {
        try {
            JSONArray routes = response.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONObject geometry = route.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            List<LatLng> decodedCoordinates = parseGeoJSONCoordinates(coordinates);
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(decodedCoordinates)
                    .color(Color.RED)
                    .width(12);
            currentRoute = gMaps.addPolyline(polylineOptions);
            dialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<LatLng> parseGeoJSONCoordinates(JSONArray coordinates) throws JSONException {
        List<LatLng> decodedCoordinates = new ArrayList<>();
        for (int i = 0; i < coordinates.length(); i++) {
            JSONArray coordPair = coordinates.getJSONArray(i);
            double latitude = Double.parseDouble(coordPair.getString(1).replace("[", "").replace("]", ""));
            double longitude = Double.parseDouble(coordPair.getString(0).replace("[", "").replace("]", ""));
            decodedCoordinates.add(new LatLng(latitude, longitude));
        }
        return decodedCoordinates;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
        userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (gMaps != null) {
            gMaps.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
        }
    }
}