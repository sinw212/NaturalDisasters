package com.example.ppnd;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShelterActivity extends AppCompatActivity {
    StringBuilder urlBuilder;

    String serviceKey_Decoder;
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

        // request queue 는 앱이 시작되었을 때 한 번 초기화되기만 하면 계속 사용이 가능
        if(AppHelper.requestqueue == null)
            AppHelper.requestqueue = Volley.newRequestQueue(this);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }
        else{
            init();
        }


        earthquakeRequest();
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
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location1 != null){
            setMyLocation(location1);
        }
        else
        {
            /*if(location2 != null){
                setMyLocation(location2);
            }*/
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

        // 무더위 api..
        // getAddress(location.getLatitude(),location.getLongitude());
        getAddress(35.10653611111111,129.02250833333335);


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

    // 지진 대피소 api
    private void earthquakeRequest(){
        final String serviceKey = "";

        try {
            serviceKey_Decoder  = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            Log.v("서비스 키 ", serviceKey_Decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        urlBuilder = new StringBuilder("http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList"); //*URL*//*

        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(serviceKey_Decoder,"UTF-8"));
            // urlBuilder.append("&" + URLEncoder.encode("WGS84_LON","UTF-8") + "=" + URLEncoder.encode(xcord, "UTF-8"));
            // urlBuilder.append("&" + URLEncoder.encode("WGS84_LAT","UTF-8") + "=" + URLEncoder.encode(ycord, "UTF-8"));
            // urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("flag","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        URI uri = null;
        try {
            uri = new URI(String.valueOf(urlBuilder));
            Log.v("uri 변환",uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for(int i = 1; i<=5; i++) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, uri.toString() + "&pageNo=" + i,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("결과", response);
                            try {

                                JSONArray jarray = new JSONObject(response).getJSONArray("EarthquakeIndoors");
                                JSONObject jobject = jarray.getJSONObject(1);
                                JSONArray row = jobject.getJSONArray("row");

                                int size = row.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject earthquake_json = row.getJSONObject(i);

                                    String address = earthquake_json.optString("ctprvn_nm") + " " + earthquake_json.optString("sgg_nm") +
                                           " " + earthquake_json.optString("vt_acmdfclty_nm");

                                    Double xcord = earthquake_json.optDouble("xcord");
                                    Double ycord = earthquake_json.optDouble("ycord");

                                    MarkerOptions makerOptions = new MarkerOptions();

                                    LatLng now_loc = new LatLng(ycord, xcord);

                                    makerOptions
                                            .position(now_loc)
                                            .title(address);

                                    // 마커를 생성한다.
                                    map.addMarker(makerOptions);

                                    MyItem offsetItem = new MyItem(ycord,xcord);
                                    mClusterManager.addItem(offsetItem);



                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("통신 에러", "[" + error.getMessage() + "]");
                            Log.v("통신 에러 이유", error.getStackTrace().toString());
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    return params;

                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 30000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 30000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            // 캐시 데이터 가져오지 않음 왜냐면 기존 데이터 가져올 수 있기때문
            // 항상 새로운 데이터를 위해 false
            stringRequest.setShouldCache(false);
            AppHelper.requestqueue.add(stringRequest);

        }

    }

    // 위도, 경도를 주소로 변환
    private void getAddress( double lat, double lng ){
        Geocoder geocoder = new Geocoder(this);

        List<Address> address = null;
        String area = null;
        try {

            address = geocoder.getFromLocation(
                    lat, // 위도
                    lng, // 경도
                    10); // 얻어올 값의 개수

            if(address!=null){
                if(address.size()==0){
                }else{
                    area = address.get(0).getAddressLine(0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }

        areaCodeRequest(area);

    }


    // 지역 코드
    private void areaCodeRequest(String area){
        String arr_area[] = area.split(" ");

        String [] url = {"http://www.kma.go.kr/DFSROOT/POINT/DATA/top.","http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.","http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."};

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            Log.v("크기", String.valueOf(size));
                            for (int i = 0; i < size; i++) {
                                JSONObject jsonObject = jarray.getJSONObject(i);
                                String code = jsonObject.getString("code");
                                String value = jsonObject.getString("value");


                                Log.v("지역코드", code + value);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("통신 에러", "[" + error.getMessage() + "]");
                        Log.v("통신 에러 이유", error.getStackTrace().toString());
                        error.printStackTrace();
                    }
                }
        ) {

            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;

            }
        };


        // 캐시 데이터 가져오지 않음 왜냐면 기존 데이터 가져올 수 있기때문
        // 항상 새로운 데이터를 위해 false
        stringRequest.setShouldCache(false);
        AppHelper.requestqueue.add(stringRequest);

    }




}

