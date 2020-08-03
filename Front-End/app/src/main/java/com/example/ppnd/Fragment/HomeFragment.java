package com.example.ppnd.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.Adapter.LocationAdapter;
import com.example.ppnd.Data.LocationData;
import com.example.ppnd.Other.DataParsing;
import com.example.ppnd.Other.LocationCode;
import com.example.ppnd.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    // GPS
    final int REQUEST_CODE_LOCATION=2;
    private LocationManager locationManagerGPS;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private List<Address> addresses;

    private RequestQueue requestQueue;

    private RecyclerView recyclerview = null;
    private LinearLayoutManager layoutManager = null;
    private LocationAdapter currentlocationAdapter;
    private ArrayList<LocationData> arrayList;
    private LocationData currentlocationData;

    private TextView current_location;
    private Button btn_earthquake, btn_typhoon, btn_thunder,
            btn_hitwave, btn_rain, btn_snow, btn_emergency;

    private String data;
    private String current_address = null;
    private int current_code;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        currentlocationAdapter = new LocationAdapter(arrayList);
        recyclerview.setAdapter(currentlocationAdapter);

        //GPS 미 설정시 false, 설정 시 true
        if (!locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS 설정 off일 시 설정화면으로 이동
            new AlertDialog.Builder(getActivity())
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
                            getActivity().finish();
                        }
                    }).show();
        }
        else // GPS 설정 on일 시 기능 수행 가능
        {
            //GPS 위도 경도 기반 위치 확인
            LocationTransmission();
        }

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        //지진 버튼
        btn_earthquake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지진 설명, 행동요령, 대피소
            }
        });

        //태풍 버튼
        btn_typhoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태풍 설명, 행동요령, 대피소
            }
        });

        //낙뢰 버튼
        btn_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //낙뢰 설명, 행동요령, 대피소
            }
        });

        //폭염 버튼
        btn_hitwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭염 설명, 행동요령, 대피소
            }
        });

        //호우 버튼
        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //호우 설명, 행동요령, 대피소
            }
        });

        //폭설 버튼
        btn_snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭설 설명, 행동요령, 대피소
            }
        });

        //119 신고버튼 (GPS 기능 필요없음)
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Call = new Intent(Intent.ACTION_CALL);
                Call.setData(Uri.parse("tel:010-9173-8332"));
                startActivity(Call);
            }
        });
    }

    //리스너를 통해서 위치값을 업데이트 하여 위치 수신 진행
    private LocationListener locationListener = new LocationListener()
    {
        // 위치값이 갱신되면 이벤트 발생
        // 위치 제공자 GPS:위성 수신으로 정확도가 높다, 실내사용 불가,위치 제공자,  Network:인터넷 엑세스 수신으로 정확도가 아쉽다, 실내 사용 가능
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                System.err.println("homeFragment IOException error");
            }
            if (addresses.size() != 0) {
                current_address = LocationCode.currentAddress(addresses.get(0).getAddressLine(0));
                current_location.setText(current_address + " 속보");
                current_code = LocationCode.currentLocationCode(current_address);
            } else { //주소를 찾지 못한 경우
                current_location.setText("주소를 찾지 못했습니다.");
//                current_location.setText(location.getLongitude() + " , " + location.getLatitude());
            }
            locationManager.removeUpdates(locationListener); // 위치 업데이트 종료

            //XML 파싱해서 Adapter에 연결하는 과정
            //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //GPS기준 현재 위치 속보 받아오기
                    data = DataParsing.newsflashXmlData(current_code); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                    //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                    //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String split_data[] = data.split("\n");
                            for(int i=0; i<split_data.length; i++) {
                                currentlocationData = new LocationData(split_data[i]);

                                arrayList.add(currentlocationData); // RecyclerView의 마지막 줄에 삽입
                                currentlocationAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
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
        locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
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

    private void initView() {
        current_location = getView().findViewById(R.id.current_location);
        recyclerview = getView().findViewById(R.id.recyclerview_current_location);
        btn_earthquake = getView().findViewById(R.id.btn_earthquake); //지진 버튼
        btn_typhoon = getView().findViewById(R.id.btn_typhoon); //태풍 버튼
        btn_thunder = getView().findViewById(R.id.btn_thunder); //낙뢰 버튼
        btn_hitwave = getView().findViewById(R.id.btn_hitwave); //폭염 버튼
        btn_rain = getView().findViewById(R.id.btn_rain); //호우 버튼
        btn_snow = getView().findViewById(R.id.btn_snow); //폭설 버튼
        btn_emergency = getView().findViewById(R.id.btn_emergency); //119 신고 버튼

        //GPS ON/OFF 확인하여 OFF일 시 GPS 설정화면으로 이동진행
        locationManagerGPS = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    }
}