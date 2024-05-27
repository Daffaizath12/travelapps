package com.example.travelapps.sopir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.travelapps.Adapter.PenumpangAdapter;
import com.example.travelapps.Adapter.PenumpangAdapterActive;
import com.example.travelapps.Adapter.PerjalananSopirAdapter;
import com.example.travelapps.Model.PemesananSopir;
import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Model.TiketSopir;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MapsSopirActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, PenumpangAdapterActive.OnStatusUpdateListener {

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
    private List<PemesananSopir> pemesananSopirList;
    private PenumpangAdapterActive adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_sopir);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        RecyclerView recyclerView = findViewById(R.id.rvUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pemesananSopirList = new ArrayList<>();
        adapter = new PenumpangAdapterActive(this, pemesananSopirList, this);
        recyclerView.setAdapter(adapter);
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

    private void locationJemputTujuanUser() {
        ApiServicesSopir.getPenumpangSopirActive(MapsSopirActivity.this, idSopir, idPerjalanan, new ApiServicesSopir.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<PemesananSopir> pemesananSopir) {
                gMaps.clear();

                for (PemesananSopir pemesanan : pemesananSopir) {
                    LatLng latLngJemput = new LatLng(
                            Double.parseDouble(pemesanan.getLatitude()),
                            Double.parseDouble(pemesanan.getLongitude())
                    );
                    double distance = calculateDistance(userLocation.latitude, userLocation.longitude, latLngJemput.latitude, latLngJemput.longitude);
                    pemesanan.setDistance(distance);
                }

                Collections.sort(pemesananSopir, new Comparator<PemesananSopir>() {
                    @Override
                    public int compare(PemesananSopir p1, PemesananSopir p2) {
                        return Double.compare(p1.getDistance(), p2.getDistance());
                    }
                });

                for (PemesananSopir pemesanan : pemesananSopir) {
                    String namaLengkap = pemesanan.getNamaLengkap();
                    gMaps.addMarker(new MarkerOptions().position(userLocation).title("Lokasi anda"));
                    LatLng latLngJemput = new LatLng(
                            Double.parseDouble(pemesanan.getLatitude()),
                            Double.parseDouble(pemesanan.getLongitude())
                    );
                    gMaps.addMarker(new MarkerOptions()
                            .position(latLngJemput)
                            .title("Penjemputan: " + namaLengkap)
                            .snippet("Alamat Jemput: " + pemesanan.getAlamatJemput()));

                    LatLng latLngTujuan = new LatLng(
                            Double.parseDouble(pemesanan.getLatTujuan()),
                            Double.parseDouble(pemesanan.getLngTujuan())
                    );
                    gMaps.addMarker(new MarkerOptions()
                            .position(latLngTujuan)
                            .title("Tujuan: " + namaLengkap)
                            .snippet("Alamat Tujuan: " + pemesanan.getAlamatTujuan()));

                    getRouteFromOSRM(userLocation, latLngJemput);

                    getRouteFromOSRM(userLocation, latLngTujuan);
                }

                pemesananSopirList.clear();
                pemesananSopirList.addAll(pemesananSopir);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message) {
                if (message.equals("Data tidak ditemukan")) {
                    destinationLocation();
                } else {
                Toast.makeText(MapsSopirActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
            }
            }
        });
    }
    private void destinationLocation(){
        ApiServicesSopir.getPenumpangSopir(MapsSopirActivity.this, idSopir, idPerjalanan, new ApiServicesSopir.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<PemesananSopir> pemesananSopir) {
                gMaps.clear();
                for (PemesananSopir pemesanan : pemesananSopir) {
                    String namaLengkap = pemesanan.getNamaLengkap();
                    gMaps.addMarker(new MarkerOptions().position(userLocation).title("Lokasi anda"));

                    LatLng latLngTujuan = new LatLng(
                            Double.parseDouble(pemesanan.getLatTujuan()),
                            Double.parseDouble(pemesanan.getLngTujuan())
                    );
                    gMaps.addMarker(new MarkerOptions()
                            .position(latLngTujuan)
                            .title("Tujuan: " + namaLengkap)
                            .snippet("Alamat Tujuan: " + pemesanan.getAlamatTujuan()));
                    
                    getRouteFromOSRM(userLocation, latLngTujuan);
                    pemesananSopirList.clear();
                    pemesananSopirList.addAll(pemesananSopir);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String message) {

            }
        });
    }
    private void locationUser() {
        ApiServicesSopir.getPenumpangSopirActive(MapsSopirActivity.this, idSopir, idPerjalanan, new ApiServicesSopir.PerjalananResponseListener() {
            @Override
            public void onSuccess(List<PemesananSopir> pemesananSopir) {
                List<LatLng> coordinates = new ArrayList<>();
                double shortestDistance = Double.MAX_VALUE;
                LatLng nearestMarkerLatLng = null;

                for (PemesananSopir pemesanan : pemesananSopir) {
                    double latitude = Double.parseDouble(pemesanan.getLatitude());
                    double longitude = Double.parseDouble(pemesanan.getLongitude());
                    LatLng markerLatLng = new LatLng(latitude, longitude);
                    coordinates.add(markerLatLng);

                    float[] distanceResult = new float[1];
                    Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                            latitude, longitude, distanceResult);
                    double distance = distanceResult[0];

                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        nearestMarkerLatLng = markerLatLng;
                    }
                    String username = pemesanan.getNamaLengkap();
                    gMaps.addMarker(new MarkerOptions().position(markerLatLng).title(username));
                }

                if (nearestMarkerLatLng != null) {
                    if (currentRoute != null) {
                        currentRoute.remove();
                    }
                    getRouteFromOSRM(userLocation, nearestMarkerLatLng);
                }
                pemesananSopirList.clear();
                pemesananSopirList.addAll(pemesananSopir);
                adapter.notifyDataSetChanged();
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
        LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMaps.addMarker(new MarkerOptions().position(sydney).title("Lokasi Anda"));
        float zoomLevel = 14.0f;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoomLevel).build();
        gMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        locationJemputTujuanUser();
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

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
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

    @Override
    public void onStatusUpdated() {
        locationJemputTujuanUser();
    }
}