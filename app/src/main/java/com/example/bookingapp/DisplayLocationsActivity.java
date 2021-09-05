package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DisplayLocationsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    double startLatitude;
    double startLongitude;
    double endLatitude;
    double endLongitude;


    private static final String TAG = "DisplayLocations";
    TextView tvdistance;
    Marker mMyFirstMarker, mySecondMarker;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient1;

    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    String sourcelocation, destinationlocation;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        //Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        if (mLocationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locations);
        getLocationPermission();

        tvdistance = findViewById(R.id.edt_display_distance);


    }

    public void getPoints() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        sourcelocation = extras.getString("source");
        destinationlocation = extras.getString("destination");

        if (sourcelocation != null && destinationlocation != null) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = new ArrayList<>();
            try {
                list = geocoder.getFromLocationName(sourcelocation, 1);
            }catch (IOException e){
                Log.e(TAG, "Getlocation: IOException: " + e.getMessage() );
            }


                if (list.size() > 0) {
                    Address address = list.get(0);
                    startLatitude = address.getLatitude();
                    startLongitude = address.getLongitude();



                    Geocoder geocoder1 = new Geocoder(this);
                    List<Address> list1 = new ArrayList<>();
                    try {
                        list1 = geocoder1.getFromLocationName(destinationlocation, 1);
                    }catch (IOException e){

                    }
                    if (list1.size() > 0) {
                        Address address1 = list1.get(0);
                        endLatitude = address1.getLatitude();
                        endLongitude = address1.getLongitude();


                        float[] distanceresults = new float[1];
                        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distanceresults);
                        float distance = distanceresults[0];

                        int kilometre = (int) (distance / 1000);
                        tvdistance.setText(kilometre + " km");

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String strDate = dateFormat.format(date);


                        LatLng sourcelocation = new LatLng(startLatitude, startLongitude);
                        LatLng destinationlocation = new LatLng(endLatitude, endLongitude);

                        MarkerOptions userMarker1 = new MarkerOptions().position(sourcelocation).title("Source");

                        mMyFirstMarker = mMap.addMarker(userMarker1);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sourcelocation, DEFAULT_ZOOM));

                        MarkerOptions userMarker2 = new MarkerOptions().position(destinationlocation).title("Destination");

                        mySecondMarker = mMap.addMarker(userMarker2);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sourcelocation, DEFAULT_ZOOM));
                    }
                }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void init() {

        //google api client object
        mGoogleApiClient1 = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, 1, this)
                .build();
        getPoints();
    }


    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(DisplayLocationsActivity.this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(DisplayLocationsActivity.this,
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(DisplayLocationsActivity.this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(DisplayLocationsActivity.this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: permission granted");
                            mLocationPermissionGranted = true;
                        }
                    }
                    mLocationPermissionGranted = false;
                    Log.d(TAG, "onRequestPermissionsResult: permissions failed");
                    return;

                }
            }
        }

    }
}