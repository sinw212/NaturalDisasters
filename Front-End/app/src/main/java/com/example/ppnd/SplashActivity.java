package com.example.ppnd;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ppnd.Other.EarthquakeShelterParsing;
import com.example.ppnd.Other.HeatWaveParsing;
import com.example.ppnd.Other.NewsFlashParsing;
import com.example.ppnd.Other.GPSService;
import com.example.ppnd.Other.LocationCode;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends Activity {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    private final int PERMISSIONREQUEST_RESULT=100; //콜백 호출시 requestcode로 넘어가는 구분자

    private int checkSecurity = 0;

    public String current_location_newsflash, nation_wide_newsflash;
    private byte[] satellite_image;
    private NewsFlashParsing newsFlashParsing = new NewsFlashParsing();
    private EarthquakeShelterParsing eqsParsing = new EarthquakeShelterParsing();
    private HeatWaveParsing hwParsing = new HeatWaveParsing();

    private String full_address, current_address;
    private int current_code;
    private String[] arr_area;
    private String Local1, Local2;
    private int count=3;
    private GPSService gpsService;

    // 지역코드 csv 파일에서 받아오기
    private ArrayList list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        gpsService = new GPSService(SplashActivity.this);
        double latitude = GPSService.getLatitude(); //위도
        double longitude = GPSService.getLongitude(); //경도
        Log.d("진입위도", String.valueOf(latitude));
        Log.d("진입경도", String.valueOf(longitude));

        full_address = getCurrentAddress(latitude, longitude); //전체 주소
        Log.d("진입full_address", full_address);
        current_address = LocationCode.currentAddress(full_address);
        Log.d("진입current_address", current_address);
        current_code = LocationCode.currentLocationCode(current_address);
        Log.d("진입current_code", String.valueOf(current_code));

        Log.d("현위치 주소",full_address);
        arr_area = full_address.split(" ");
        Local1 = arr_area[1]; //OO도
        Local2 = arr_area[2]; //OO시

        // 창원시 ~ 전주시는 ㅇㅇ시ㅇㅇ구로 표현되기 때문에 따로 검사
        String city[] = {"창원시","수원시","성남시","안양시","안산시","고양시","용인시","청주시","천안시","전주시"};
        int size = city.length;
        for(int i =0; i<size; i++){
            if(Local2.equals(city[i])){
                Local2 += arr_area[3];
                count++;
            }
        }
        while(true) {
            try {
                InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.areacode));
                BufferedReader reader = new BufferedReader(is);
                CSVReader read = new CSVReader(reader);
                String record = null;
                boolean flag = false;
                while ((record = reader.readLine()) != null) {
                    String arr[] = record.split("/");

                    if (flag) {
                        if (arr.length < 4)
                            break;
                        else {
                            if (!arr[2].equals(Local2))
                                break;
                        }
                    }
                    if (arr.length == 4) {
                        if (arr[1].equals(Local1)) {
                            if (arr[2].equals(Local2)) {
                                list.add(arr[0]);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(list.size() >0)
                break;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED ||
                        ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED||
                        ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_DENIED)) {
            Log.d("진입1", "ㅇㅇ");
            ProgressTask task = new ProgressTask();
            Log.d("진입2", "ㅇㅇ");
            task.execute();
        }
        else{
            Log.d("진입MainActivity로 넘어가", "ㅇㅇ");
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //권한 미허용으로 인해 '권한 허용' 액티비티 연결
            finish(); // 해당 액티비티 종료
        }
    }

    //속보&위성사진 & 지진대피소&무더위쉼터 위치 파싱
    public class ProgressTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this);
        //가장 먼저 호출
        @Override
        protected void onPreExecute() {
            Log.d("진입3", "ㅇㅇ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("설정중입니다");
            progressDialog.setCanceledOnTouchOutside(false); // 프로그래스 끄기 방지
            progressDialog.show();
            super.onPreExecute();
        }

        //작업 스레드를 실행하는 함수
        //메인 스레드와는 별개로 오래 걸리는 작업 처리
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("진입4", "ㅇㅇ");
            try {
                for (int i = 1; i < 6; i++) {
                    switch (i * 10) {
                        case 10:
//                            hwParsing.HeatWaveParsing(list, getApplicationContext());
                            Log.d("진입5", "ㅇㅇ");
                            checkSecurity += 1;
                            break;
                        case 20:
//                            eqsParsing.EarthquakeShelterParsing(Local1, Local2);
                            Log.d("진입6", "ㅇㅇ");
                            checkSecurity += 1;
                            break;
                        case 30:
                            current_location_newsflash = newsFlashParsing.newsflashXmlData(current_code);
                            Log.d("진입7", String.valueOf(current_code));
                            checkSecurity += 1;
                            break;
                        case 40:
                            nation_wide_newsflash = newsFlashParsing.newsflashXmlData(108);
                            Log.d("진입8", "ㅇㅇ");
                            checkSecurity += 1;
                            break;
                        case 50:
                            Bitmap bm = newsFlashParsing.satelliteXmlData();
                            //100K 이상 데이터를 Intent를 통해 put할 경우 에러 발생 -> 크기 줄여줘야함
                            //원인 : android os에서 intent에 100K 이상 넣을 수 없기 때문
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            satellite_image = bos.toByteArray();
                            Log.d("진입9", "ㅇㅇ");
                            checkSecurity += 1;
                        default:
                            break;
                    }
                }
                Log.d("진입10", "ㅇㅇ");
                progressDialog.setProgress(1*10);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.d("진입못해따", "ㅇㅇ");
                System.err.println("MyAsyncTask InterruptedException error ");
            }

            return null;
        }

        //doInBackground() 실행이 정상적으로 처리가 완료되는 경우 실행
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            super.onPostExecute(result);

            Log.d("진입11", "ㅇㅇ");

            if(checkSecurity == 5) {
                Log.d("진입12", "ㅇㅇ");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("current_location_newsflash", current_location_newsflash);
                intent.putExtra("nation_wide_newsflash", nation_wide_newsflash);
                intent.putExtra("satellite_image", satellite_image);
                intent.putExtra("current_address", current_address);
                Log.d("진입13", current_address);

                startActivity(intent);
                finish();
            } else {
                Log.d("진입못해따", "ㅇㅇ");
                Toast.makeText(SplashActivity.this, "실패했습니다. 다시 앱을 설치해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        //doInBackground() 실행 도중 작업이 중단되는 경우 실행
        @Override
        protected void onCancelled(Void result) {
            Log.d("진입왜???", "ㅇㅇ");
            super.onCancelled(result);
        }
    }

    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException e) {
            //네트워크 문제
            Toast.makeText(this, "geocoder 서비스 사용불가", Toast.LENGTH_SHORT).show();
            return "geocoder 서비스 사용불가";
        } catch(IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() ==0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소 미발견";
        } else {
            full_address = addresses.get(0).getAddressLine(0); //위경도 값을 주소로 변환한 전체 주소
        }
        return full_address;
    }

    void checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasBackGROUNDLocationPermission =ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        int hasCallPhoneStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasBackGROUNDLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCallPhoneStatePermission == PackageManager.PERMISSION_GRANTED){
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("Check", "onActivityResult : GPS 활성화 되어있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                else
                    Log.d("Check", "onActivityResult : GPS 활성화 !!안!!되어있음");
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //퍼미션 요청 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (requestCode == PERMISSIONREQUEST_RESULT) {
            if (grantResult.length > 0) {
                for (int aGrantResult : grantResult) {
                    if (aGrantResult == PackageManager.PERMISSION_DENIED) {
                        // 권한이 하나라도 거부 될 시
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("사용 권한의 문제발생")
                                .setIcon(R.drawable.main_icon)
                                .setMessage("저희 서비스 사용을 위해서는 서비스의 요구권한을 필수적으로 허용해주셔야합니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                                finish();
                            }
                        }).show();
                        return;
                    }
                }
            }
        }
    }
}