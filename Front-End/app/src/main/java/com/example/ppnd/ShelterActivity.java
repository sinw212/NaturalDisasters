package com.example.ppnd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ppnd.Data.EarthquakeShelterData;
import com.example.ppnd.Data.HeatWaveShelterData;
import com.example.ppnd.Other.HeatWaveParsing;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ShelterActivity extends AppCompatActivity {

    String [] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    LocationManager locationManager;
    GoogleMap map;
    ClusterManager<MyItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);

        Intent intent = getIntent();
        String type = intent.getExtras().getString("type");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }
        else{
            init();
        }


        if(type.equals("earthquake")){
            final ArrayList<EarthquakeShelterData> list = (ArrayList<EarthquakeShelterData>) intent.getSerializableExtra("earthquakeshelter");
            int size = list.size();
            Log.d("지진 대피소 크기",String.valueOf(size));
            for(int i =0; i <size; i++){
                MarkerOptions makerOptions = new MarkerOptions();

                LatLng now_loc = new LatLng(list.get(i).getLat(), list.get(0).getLng());

                makerOptions
                        .position(now_loc)
                        .title(list.get(i).getLocation());

                // 마커를 생성한다.
                map.addMarker(makerOptions);

                MyItem offsetItem = new MyItem(list.get(i).getLat(), list.get(0).getLng());
                mClusterManager.addItem(offsetItem);
            }
        }
        else if(type.equals("heatwave")){
            final ArrayList<HeatWaveShelterData> arrayList = (ArrayList<HeatWaveShelterData>) intent.getSerializableExtra("heatwaveshelter");
            int size = arrayList.size();
            for(int i =0; i<size; i++){
                Double[] arr = getLat (arrayList.get(i).getAddress());
                MarkerOptions makerOptions = new MarkerOptions();

                LatLng now_loc = new LatLng(arr[0], arr[1]);

                makerOptions
                        .position(now_loc)
                        .title(arrayList.get(i).getLoc_nm());

                // 마커를 생성한다.
                map.addMarker(makerOptions);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED) { // 하나라도 거부 상태면
                return;
            }
        }
        init();
    }

    public void init(){
        FragmentManager fragmentManager =getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);

        MapRedatCallback callback1 = new MapRedatCallback();
        mapFragment.getMapAsync(callback1);

    }

    // 구글 지도 사용 준비가 완료되면 동작하는 콜백
    class MapRedatCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            // Log.d("test123","구글 지도 사용 준비 완료");
            getMyLocation();
        }
    }


    // 현재 위치를 측정하는 메서드
    public void getMyLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ){
                return;
            }
        }

        // 이전에 측정했던 값을 가져온다.
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //GPS로 받기
        Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  // 네트워크로 받기

        if(location1 != null){
            setMyLocation(location1);
        }
        else
        {
            if(location2 != null){
                setMyLocation(location2);
            }
        }
        // 새롭게 측정한다.
        GetMyLocationListener listener = new GetMyLocationListener();

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, listener);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, listener);
        }

    }

    public void setMyLocation(Location location){
        Log.d("test123","위도 : " +location.getLatitude());
        Log.d("test123","경도 : " + location.getLongitude());


        // 위도와 경도값을 관리하는 객체
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

        CameraUpdate update1 = CameraUpdateFactory.newLatLng(position);
        CameraUpdate update2 = CameraUpdateFactory.zoomTo(15f);


        map.moveCamera(update1);
        map.animateCamera(update2);

        // 클러스터 매니저 생성
        mClusterManager = new ClusterManager<MyItem>(this,map);

        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ){
                return;
            }
        }

        // 현재 위치 표시
        map.setMyLocationEnabled(true);
    }

    // 현재 위치 측정이 성공하면 반응하는 리스너
    class GetMyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            setMyLocation(location);
            locationManager.removeUpdates(this);
        }

        @Override
        public void onProviderDisabled(String s) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    }


    // 주소를 위도, 경도로 변환
    private Double[] getLat ( String str ){
        Geocoder geocoder = new Geocoder(this);
        Double[] arr = new Double[2];

        List<Address> address = null;
        try {

            address = geocoder.getFromLocationName(
                    str,
                    10); // 얻어올 값의 개수
            if(address!= null){
                arr[0] = address.get(0).getLatitude();
                arr[1] = address.get(0).getLongitude();
                Log.d("주소 변환 위도 ",String.valueOf(arr[0]));
                Log.d("주소 변환 경도 ",String.valueOf(arr[1]));

            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }

        return arr;

    }

}

