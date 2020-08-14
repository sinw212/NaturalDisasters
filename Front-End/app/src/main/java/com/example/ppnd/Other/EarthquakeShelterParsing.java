package com.example.ppnd.Other;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.AppHelper;
import com.example.ppnd.Data.EarthquakeShelterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EarthquakeShelterParsing {
    StringBuilder urlBuilder;
    String serviceKey_Decoder;
    String serviceKey = ""; // 서비스 키

    final ArrayList<EarthquakeShelterData> arrayList = new ArrayList<>();

    int count = 0;

    public EarthquakeShelterParsing(){

        earthquakeRequest();
    }

    public void earthquakeRequest(){

        try {
            serviceKey_Decoder  = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            // Log.v("서비스 키 ", serviceKey_Decoder);
            urlBuilder = new StringBuilder("http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList"); //*URL*//*
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(serviceKey_Decoder,"UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("flag","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for(int i = 1; i<=5; i++) {
            count = i;
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET, urlBuilder.toString() + "&pageNo=" + i,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("결과", response);
                            try {

                                JSONArray jarray = new JSONObject(response).getJSONArray("EarthquakeIndoors");
                                JSONObject jobject = jarray.getJSONObject(1);
                                JSONArray row = jobject.getJSONArray("row");

                                int size = row.length();

                                for (int j = 0; j < size; j++) {
                                    JSONObject earthquake_json = row.getJSONObject(j);

                                    String ctprvn_nm = earthquake_json.optString("ctprvn_nm");
                                    String sgg_nm = earthquake_json.optString("sgg_nm");
                                    String vt_acmdfclty_nm = earthquake_json.optString("vt_acmdfclty_nm");

                                    Double xcord = earthquake_json.optDouble("xcord");
                                    Double ycord = earthquake_json.optDouble("ycord");

                                    arrayList.add(new EarthquakeShelterData(vt_acmdfclty_nm, ycord, xcord));


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

    public ArrayList getArrayList(){
        return arrayList;
    }
}
