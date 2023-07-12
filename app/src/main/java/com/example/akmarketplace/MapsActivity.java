package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.akmarketplace.databinding.ActivityMaps2Binding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private LocationListener locationListener;//mainly for onLocationChanged
    private LocationManager locationManager;//for requesting updates and checking if GPS is on
    private final long MIN_TIME = 1000; // min of 1 sec between GPS readings
    private final long MIN_DIST = 0; // if new reading with x meters then do not show it

    private double itemLocLat;
    private double itemLocLng;

    private double currLocLat;
    private double currLocLng;

    private double halfwayLat;
    private double halfwayLng;
    private double diffLat;
    private double diffLng;

    private float zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.d("MAPTEST", "end of oncreate");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker in Sydney and move the camera



        Intent intentPassed = getIntent();

        itemLocLat = intentPassed.getDoubleExtra("locLat", 0);
        itemLocLng = intentPassed.getDoubleExtra("locLng", 0);
        LatLng itemLocation = new LatLng(itemLocLat, itemLocLng);
        mMap.addMarker(new MarkerOptions().position(itemLocation).title("Meetup Location Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 15.0f));


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CMP354---","locationManager.removeUpdates(locationListener)");
    }

    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 202) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //now we have the user permission, so let us start all over again
                onMapReady(mMap);
            }
            else
                finish(); //no point continuing with the app
        }
    }
}