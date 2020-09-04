package com.example.ppnd;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ppnd.Other.DataParsing;
import com.example.ppnd.Other.LocationCode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends Activity {
    // GPS
    private final int REQUEST_CODE_LOCATION=2;
    private LocationManager locationManagerGPS, locationManager;
    private Geocoder geocoder;
    private List<Address> addresses;

    private String full_address, current_address;
    private int current_code;

    private String current_location_newsflash,nation_wide_newsflash;
    private Bitmap satellite_image;
    private int checkSecurity = 0;
    private DataParsing dataParsing = new DataParsing();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GPS ON/OFF 확인하여 OFF일 시 GPS 설정화면으로 이동진행
        locationManagerGPS = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //GPS 미 설정시 false, 설정 시 true
        if (!locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS 설정 off일 시 설정화면으로 이동
            new AlertDialog.Builder(getApplicationContext())
                    .setCancelable(false)
                    .setTitle("GPS 미설정 알림")
                    .setIcon(R.drawable.main_icon)
                    .setMessage("GPS가 미설정 되어있어, 위급 시 속보 알림을 받지 못할 우려가 있으니 GPS 사용을 허용해주세요\n" +
                            "허용한 후, 원활한 사용을 위해 앱을 다시 켜주십시오.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            setting.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(setting);
                            getParent().finish();
                        }
                    }).show();
        }
        else // GPS 설정 on일 시 기능 수행 가능
        {
            //GPS 위도 경도 기반 위치 확인
            LocationTransmission();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_DENIED)) {

            PrograssTask task = new PrograssTask();
            task.execute();
        }
        else{
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //권한 미허용으로 인해 '권한 허용' 액티비티 연결
            finish(); // 해당 액티비티 종료
        }
    }

    private class PrograssTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(
                SplashActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("설정중입니다");
            progressDialog.setCanceledOnTouchOutside(false); // 프로그래스 끄기 방지
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < 10; i++) {
                    switch (i * 10) {
                        case 10:
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            current_location_newsflash = dataParsing.newsflashXmlData(current_code);
                            Log.d("진입11", String.valueOf(current_code));
                            checkSecurity += 1;
                            break;
                        case 20:
                            nation_wide_newsflash = dataParsing.newsflashXmlData(108);
                            checkSecurity += 1;
                            break;
                        case 30:
                            satellite_image = dataParsing.satelliteXmlData();
                            checkSecurity += 1;
                        default:
                            break;
                    }
                    progressDialog.setProgress(i * 10);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                System.err.println("SplashActivity InterruptedException error ");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            super.onPostExecute(result);
            if (checkSecurity == 3) { // 수정필요 추후
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("current_location_newsflash", current_location_newsflash);
                intent.putExtra("nation_wide_newsflash", nation_wide_newsflash);
                intent.putExtra("satellite_image", satellite_image);
                intent.putExtra("current_address", current_address);

                Log.d("진입11", current_address);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SplashActivity.this, "실패했습니다. 다시 앱을 설치해주세요.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    //리스너를 통해서 위치값을 업데이트 하여 위치 수신 진행
    private LocationListener locationListener = new LocationListener()
    {
        // 위치값이 갱신되면 이벤트 발생
        // 위치 제공자 GPS:위성 수신으로 정확도가 높다, 실내사용 불가,위치 제공자,  Network:인터넷 엑세스 수신으로 정확도가 아쉽다, 실내 사용 가능
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                System.err.println("IOException error");
            }
            if (addresses.size() != 0) {
                full_address = addresses.get(0).getAddressLine(0);
                current_address = LocationCode.currentAddress(full_address);
                current_code = LocationCode.currentLocationCode(current_address);
            } else { //주소를 찾지 못한 경우
                System.err.println(location.getLongitude() + " , " + location.getLatitude());
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
    private void LocationTransmission() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
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
                System.err.println("SecurityException error ");
            }
        } else {
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
                System.err.println("SecurityException error ");
            }
        }
    }
}