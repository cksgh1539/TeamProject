package com.example.hp.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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

/**
 * Created by A on 2017-12-05.
 */

public class RSmap extends AppCompatActivity implements OnMapReadyCallback {
    final private String TAG = "LocationService";
    private FusedLocationProviderClient mFusedLocationClient;

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;

    private Location mLastLocation;
    GoogleMap mGoogleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button find = (Button)findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
    }

    private boolean checkLocationPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                RSmap.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
                break;
            }
        }
    }

    //현재 위치 받아오기----------------------------------------------------------------------
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Task task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mLastLocation = location;
                    updateUI();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.no_location_detected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //위치 변경, 이동-------------------------------------------------------------------------
    private void updateUI() {
        double latitude = 0.0;
        double longitude = 0.0;

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        TextView mResultText = (TextView) findViewById(R.id.result);
        mResultText.setText(String.format("[ %s , %s ]",latitude,longitude));

        LatLng location = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(
                new MarkerOptions().
                        position(location));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    //지도에 나타내기-------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng location = new LatLng(37.5817891, 127.009854);

        // move the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    //위치 검색------------------------------------------------------------------------------
    private void getAddress() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);

            EditText input = (EditText)findViewById(R.id.edit_text) ;
            List<Address> addresses = geocoder.getFromLocationName(input.getText().toString(),1);
            if (addresses.size() >0) {
                Address bestResult = (Address) addresses.get(0);

                mLastLocation.setLatitude(bestResult.getLatitude());
                mLastLocation.setLongitude(bestResult.getLongitude());
                updateUI();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(),"Failed in using Geocoder.", e);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.current_location, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.current_location:
                if (!checkLocationPermissions()) {
                    requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
                } else
                    getLastLocation();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
