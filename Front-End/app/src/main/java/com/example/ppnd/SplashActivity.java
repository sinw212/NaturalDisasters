package com.example.ppnd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.volley.toolbox.Volley;
import com.example.ppnd.Other.EarthquakeShelterParsing;
import com.example.ppnd.Other.HeatWaveParsing;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;


public class SplashActivity extends Activity {

    // GPS
    final int REQUEST_CODE_LOCATION=2;
    private LocationManager locationManagerGPS;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private List<Address> addresses;

    EarthquakeShelterParsing earthquakeShelterParsing;
    HeatWaveParsing heatWaveParsing;

    String area = ""; // 한글 주소



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        // 지진 대피소 api
        // request queue 는 앱이 시작되었을 때 한 번 초기화되기만 하면 계속 사용이 가능
        if(AppHelper.requestqueue == null)
            AppHelper.requestqueue = Volley.newRequestQueue(this);

        earthquakeShelterParsing = new EarthquakeShelterParsing();

        Intent intent = new Intent(this, MainActivity.class);
        while(true){
            if(earthquakeShelterParsing.getArrayList().size() > 100){
                break;
            }
        }

        // 무더위 대피소 api
        // 지역코드 변환
        String[] arr_area = area.split(" ");

        Log.d("주소1 :",arr_area[1]);
        Log.d("주소2 :",arr_area[2]);

        String Local1 = arr_area[1];
        String Local2 = arr_area[2];

        if(Local2.equals("창원시"))// 창원시는 창원시 ㅇㅇ구로 표현되기 때문에 따로 검사
            Local2 += " "+arr_area[3];
        else{// 수원시 ~ 전주시는 ㅇㅇ시ㅇㅇ구로 표현되기 때문에 따로 검사
            String city[] = {"수원시","성남시","안양시","안산시","고양시","용인시","청주시","천안시","전주시"};

            int size = city.length;
            for(int i =0; i<size; i++){
                if(Local2.equals(city[i]))
                    Local2 += arr_area[3];
            }
        }

        // 지역코드 csv 파일에서 받아오기
        ArrayList list = new ArrayList<>();

        try {
            InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.areacode));
            BufferedReader reader = new BufferedReader(is);
            CSVReader read = new CSVReader(reader);
            String record = null;
            boolean flag = false;
            while ((record = reader.readLine()) != null){
                String arr[] = record.split("/");
                // Log.d("레코드",record);

                if(flag){
                    if(arr.length < 4)
                        break;
                    else {
                        if (!arr[2].equals(Local2))
                            break;
                    }
                }
                if(arr.length == 4) {
                    if (arr[1].equals(Local1)) {
                        if (arr[2].equals(Local2)) {
                            list.add(arr[0]);
                            Log.d("가져온 지역코드", arr[0]);
                            Log.d("시", arr[2]);
                            flag = true;
                        }
                    }
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        heatWaveParsing = new HeatWaveParsing(list);
        intent.putExtra("earthquakeshelter", earthquakeShelterParsing.getArrayList());
        intent.putExtra("heatwaveshelter", heatWaveParsing.getArrayList());
        startActivity(intent);
        finish();
    }


    //리스너를 통해서 위치값을 업데이트 하여 위치 수신 진행
    private LocationListener locationListener = new LocationListener()
    {
        // 위치값이 갱신되면 이벤트 발생
        // 위치 제공자 GPS:위성 수신으로 정확도가 높다, 실내사용 불가,위치 제공자,  Network:인터넷 엑세스 수신으로 정확도가 아쉽다, 실내 사용 가능
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                System.err.println("homeFragment IOException error");
            }

            locationManager.removeUpdates(locationListener); // 위치 업데이트 종료
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    //업데이트된 위치 값을 얻어와 수신
    private void LocationTransmission(){
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            // 무조건 퍼미션을 허용한다는 전제조건하에 진행 (필수적 권한)
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치 제공자
                        100, // 통지사이의 최소 시간 간격
                        1, // 통지사이의 최소 변경 거리
                        locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치 제공자
                        100, // 통지사이의 최소 시간 간격
                        1, // 통지사이의 최소 변경 거리
                        locationListener);
            } catch (SecurityException e) {
                System.err.println("mainFragment Perm ok SecurityException error ");
            }
        }
        else{
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치 제공자
                        1000, // 통지사이의 최소 시간 간격
                        1, // 통지사이의 최소 변경 거리
                        locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치 제공자
                        1000, // 통지사이의 최소 시간 간격
                        1, // 통지사이의 최소 변경 거리
                        locationListener);
            } catch (SecurityException e) {
                System.err.println("mainFragment Not Perm ok SecurityException error ");
            }
        }
    }

}
