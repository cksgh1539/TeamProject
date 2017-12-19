package com.example.hp.teamproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RSmap extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private RSdbHelper rsDbHelper;

    SharedPreferences setting;
    public static final String OptionMenu = "kilometer";

    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;

    private Location mLastLocation;
    GoogleMap mGoogleMap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_map);

        rsDbHelper = new RSdbHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
    }


    //권한 체크------------------------------------------------------------------------------------
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


    //지도에 나타내기------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        getLastLocation();
        startLocationUpdates();

        mGoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(100000);
        locRequest.setFastestInterval(50000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLastLocation = locationResult.getLastLocation();
                updateUI();
            }
        };

        mFusedLocationClient.requestLocationUpdates(locRequest,mLocationCallback,null /* Looper */);
    }


    //현재 위치 받아오기----------------------------------------------------------------------------
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Task task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mLastLocation = location;
                    CalculationByDistance(1000); //default: 1km이내에 등록된 맛집 표시
                    updateUI();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.no_location_detected), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //위치 변경, 이동-----------------------------------------------------------------------------
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


    //액션바 실행-----------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.current_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //반경(km) 선택
    public boolean onOptionsItemSelected(MenuItem item) {
        setting = getSharedPreferences(OptionMenu,MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

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
                item.setChecked(true);
                editor.putBoolean("ONE",true).commit();
                CalculationByDistance(1000); //1km이내에 등록된 맛집 표시
                break;
            case R.id.two_km:
                item.setChecked(true);
                editor.putBoolean("TWO",true).commit();
                CalculationByDistance(2000);
                break;
            case R.id.three_km:
                item.setChecked(true);
                editor.putBoolean("THREE",true).commit();
                CalculationByDistance(3000);
                break;
            case R.id.viewAll:
                item.setChecked(true);
                editor.putBoolean("ALL",true).commit();
                CalculationByDistance(100000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //현재 위치로부터의 반경 계산-------------------------------------------------------------------
    // http://hashcode.co.kr/questions/1819 참조
    private void CalculationByDistance(int Meter) {
        Cursor mark = rsDbHelper.getRSByMethod();
        double markLat,markLong,distance;

        Location place = new Location(" ");
            mGoogleMap.clear();
            while (mark.moveToNext()) {
                markLat = Double.valueOf(mark.getString(5)).doubleValue(); //db에 저장된 위도값
                markLong = Double.valueOf(mark.getString(6)).doubleValue(); //db에 저장된 경도값
                place.setLatitude(markLat);
                place.setLongitude(markLong);
                distance = mLastLocation.distanceTo(place);
                if(distance<Meter){
                    LatLng current_markLocate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mGoogleMap.addMarker( new MarkerOptions().
                            position(current_markLocate).
                            title(mark.getString(2)));

                    LatLng markLocate = new LatLng(markLat, markLong);
                    mGoogleMap.addMarker(
                            new MarkerOptions().
                                    position(markLocate).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).
                                    title(mark.getString(2))
                    );
                }
            }

    }

    
    //위치 검색------------------------------------------------------------------------------------
    private void getAddress() {
        try {
            EditText input = (EditText) findViewById(R.id.edit_text);
            Cursor name = rsDbHelper.getRSbyName(input.getText().toString());
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);

            List<Address> addresses = geocoder.getFromLocationName(input.getText().toString(), 1);
            if (addresses.size() > 0) {
                Address bestResult = addresses.get(0);

                mLastLocation.setLatitude(bestResult.getLatitude());
                mLastLocation.setLongitude(bestResult.getLongitude());
                updateUI();
            }
            if(name.getCount()!= 0) { //이름으로 검색
                name.moveToNext();
                mLastLocation.setLatitude(Double.valueOf(name.getString(5)).doubleValue());
                mLastLocation.setLongitude(Double.valueOf(name.getString(6)).doubleValue());
                updateUI();
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }
    }


    //마커 클릭시--------------------------------------------------------------------------------
    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng mlocation = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
            //클릭한 마커 위치값

            Cursor location = rsDbHelper.getRSbyLocation(String.valueOf(mlocation.latitude),String.valueOf(mlocation.longitude));
            //마커위치와 db에 저장된 식당 위치 비교
            if (location.getCount() != 0) { //같은 위치인 맛집이 있을 경우
                location.moveToNext();
                Intent detailRS = new Intent(getApplicationContext(), MainRestaurant.class);
                detailRS.putExtra("markerlatitude",location.getString(5));
                detailRS.putExtra("markerlongitude",location.getString(6));
                detailRS.putExtra("INT",1);
                startActivity(detailRS);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }else{//새로 등록하는 맛집인 경우
                ResistDialog(mlocation);
            }return false;
        }
    }

    //맛집 등록 다이얼로그 창
    private void ResistDialog(final LatLng mlocation){
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
                        Intent resistRS = new Intent(getApplicationContext(), ResistRS.class);
                        resistRS.putExtra("RSlatitude",String.valueOf(mlocation.latitude));
                        resistRS.putExtra("RSlongitude",String.valueOf(mlocation.longitude));
                        startActivity(resistRS);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("맛집 등록");
        alert.show();
    }

}

