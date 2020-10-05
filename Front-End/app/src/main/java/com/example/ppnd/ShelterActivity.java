package com.example.ppnd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.ppnd.Data.EarthquakeShelterData;
import com.example.ppnd.Data.HeatWaveShelterData;
import com.example.ppnd.Other.AppHelper;
import com.example.ppnd.Other.EarthquakeShelterParsing;
import com.example.ppnd.Other.EarthquakeShelterTask;
import com.example.ppnd.Other.HeatWaveParsing;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShelterActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    String [] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    LocationManager locationManager;
    GoogleMap map;
    String type;

    View marker_root_view;
    TextView tv_marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }
        else{
            init();
        }


    }



    // 구글 맵
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

        getData(type);

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

    // 마커

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }

    private Marker addMarker(Double lat, Double lng, String title) {
        LatLng position = new LatLng(lat, lng);
        tv_marker.setText(title);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));

        return map.addMarker(markerOptions);
    }


    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String title = marker.getTitle();

        return addMarker(lat, lon, title);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        map.animateCamera(center);

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    private void getData(String type){

        if(type.equals("earthquake")){
            final ArrayList<EarthquakeShelterData> list = EarthquakeShelterTask.getArrayList();
            int size = list.size();
            Log.d("지진 대피소 크기",String.valueOf(size));

            for(int i =0; i <size; i++){
                // 마커를 생성한다.
                addMarker(list.get(i).getLat(), list.get(i).getLng(), list.get(i).getLocation());
            }
        }
        else if(type.equals("heatwave")){
            final ArrayList<HeatWaveShelterData> list = HeatWaveParsing.getArrayList();
            int size = list.size();
            Log.d("무더위 쉼터 크기",String.valueOf(size));
            for(int i =0; i<size; i++){
                addMarker(list.get(i).getLat(), list.get(i).getLng(), list.get(i).getLocation());
            }
        }
    }

}