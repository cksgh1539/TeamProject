package com.example.hp.teamproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by A on 2017-12-05.
 */

public class RSmap extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private RSdbHelper rsDbHelper;

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;

    private Location mLastLocation;
    double distance;
    GoogleMap mGoogleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_map);

        rsDbHelper = new RSdbHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
     //   getLastLocation();
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
    }


    //권한 체크----------------------------------------------------------------------
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
            case REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
            }
        }
    }


    //옵션메뉴에서 현재 위치 받아오기--------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.current_location, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

          RSdbHelper MapDB = new RSdbHelper(this);
        double markLat,markLong;

        switch (item.getItemId()) {
            case R.id.current_location:
                if (!checkLocationPermissions()) {
                    requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
                    requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES);
                } else {
                    getLastLocation();
                    startLocationUpdates();
                }
                break;
            case R.id.one_km:  
                mGoogleMap.clear();
                Cursor mark = MapDB.getRSByMethod();
                    if(mark.moveToFirst()) {
                        while (mark.moveToNext()) {
                            markLat = Double.valueOf(mark.getString(5)).doubleValue();
                            markLong = Double.valueOf(mark.getString(6)).doubleValue();
                            CalculationByDistance(markLat, markLong);
                            if(distance<400){
                                LatLng markLocate = new LatLng(markLat, markLong);
                                mGoogleMap.addMarker(
                                        new MarkerOptions().
                                                position(markLocate).
                                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                                title(mark.getString(2))
                                );
                            }
                            Toast.makeText(this, "거리 : "+distance, Toast.LENGTH_SHORT).show();
                        }
                    }else
                        Toast.makeText(this, "실행안됨 : "+distance, Toast.LENGTH_SHORT).show();

                break;
            case R.id.two_km:


                break;
            case R.id.three_km:
                mGoogleMap.addCircle(new CircleOptions()
                        .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .radius(900)
                        .strokeColor(Color.parseColor("#884169e1"))
                        .fillColor(Color.parseColor("#5587cefa")));

                break;
        }
        return super.onOptionsItemSelected(item);



    }
    //-------------------------------마커와의 거리-------------
    // http://hashcode.co.kr/questions/1819 참조
    private void CalculationByDistance(Double aLatitude,Double aLongitude) {
        double markLat,markLong;
       markLat = Double.valueOf(aLatitude).doubleValue();
       markLong = Double.valueOf(aLongitude).doubleValue();

        Location place = new Location(" ");
        place.setLatitude(markLat);
        place.setLongitude(markLong);

        distance = mLastLocation.distanceTo(place);

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

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(5000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                updateUI();
            }
        };

        mFusedLocationClient.requestLocationUpdates(locRequest,
                mLocationCallback,
                null /* Looper */);
    }





    //위치 검색------------------------------------------------------------------------------
    private void getAddress() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);

            EditText input = (EditText) findViewById(R.id.edit_text);
            List<Address> addresses = geocoder.getFromLocationName(input.getText().toString(), 1);
            if (addresses.size() > 0) {
                Address bestResult = addresses.get(0);

                mLastLocation.setLatitude(bestResult.getLatitude());
                mLastLocation.setLongitude(bestResult.getLongitude());
                updateUI();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }
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
        mResultText.setText(String.format("[ %s , %s ]", latitude, longitude));

        LatLng location = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(
                new MarkerOptions().
                        position(location));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    //지도에 나타내기-------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getLastLocation();

        Cursor RS = rsDbHelper.getRSByMethod();
        while (RS.moveToNext()) {
            double db_latitude= Double.parseDouble(RS.getString(5));
            double db_longitude= Double.parseDouble(RS.getString(6));
            Log.i("MainRS", " :RS_ID " + Double.parseDouble(RS.getString(5)) +" "+Double.parseDouble(RS.getString(6)));
            LatLng db_location = new LatLng(db_latitude, db_longitude);
            mGoogleMap.addMarker(
                    new MarkerOptions().
                            position(db_location).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                            title(RS.getString(2))
            );
        }

        mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    //마커 클릭시----------------------------------------------------------------------------
    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            DialogSimple();
            return false;
        }
    }

    private void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("새로운 맛집으로 등록하시겠습니까?").setCancelable(
                false).setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), ResistRS.class);
                        intent.putExtra("RSlatitude",String.valueOf(mLastLocation.getLatitude()));
                        intent.putExtra("RSlongitude",String.valueOf(mLastLocation.getLongitude()));
                        startActivity(intent);
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("맛집 등록");
        alert.show();
    }

}

