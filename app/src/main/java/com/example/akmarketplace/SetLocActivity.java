package com.example.akmarketplace;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.akmarketplace.databinding.ActivityMapsBinding;

public class SetLocActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private MarkerOptions meetupLocation;
    private LatLng meetupCoordinates;




    private RunTrackerDB runTrackerDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        runTrackerDB = new RunTrackerDB(this);
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
        //mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        meetupCoordinates = new LatLng(25.310338125326922, 55.491244819864185);
        meetupLocation = new MarkerOptions().position(meetupCoordinates).title("AUS");
        mMap.addMarker(meetupLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meetupCoordinates, 15.0f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locAUS, 12.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {

                //your code goes here

                mMap.clear();

                Location loc = new Location("Google maps");

                loc.setLongitude(latLng.longitude);
                loc.setLatitude(latLng.latitude);
                loc.setTime(System.currentTimeMillis());

                //make db a member variable and in onCreate db = new RunTrackerDB(this);

                runTrackerDB.insertLocation(loc, "Meetup Location");

                meetupCoordinates = latLng;
                meetupLocation = new MarkerOptions().position(meetupCoordinates).title("Meetup Location");

                mMap.addMarker(meetupLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                AlertDialog alertDialog = new AlertDialog.Builder(SetLocActivity.this).create();
                alertDialog.setTitle("Confirm Meetup Location");
                alertDialog.setMessage("Would you like to confirm this as the meetup location?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = getIntent();
                                intent.putExtra("replyLat", meetupCoordinates.latitude);
                                intent.putExtra("replyLng", meetupCoordinates.longitude);
                                setResult(201, intent);
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();



            }
        });





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