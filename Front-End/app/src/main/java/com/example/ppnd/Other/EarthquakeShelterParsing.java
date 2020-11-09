package com.example.ppnd.Other;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ppnd.Data.EarthquakeShelterData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EarthquakeShelterParsing extends AppCompatActivity {

    private static ArrayList<EarthquakeShelterData> arrayList = new ArrayList<>();

    //지진대피소 위치 받아오기
    public static String EarthquakeShelterParsing(String url) {
        // Request Obejct인 StringRequest 생성
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject jsonObject = jarray.getJSONObject(i);
                                String ycord = jsonObject.getString("ycord");
                                String xcord = jsonObject.getString("xcord");
                                String name = jsonObject.getString("name");

                                Log.d("지진 대피소 ", name);
                                arrayList.add(new EarthquakeShelterData(name, Double.parseDouble(ycord), Double.parseDouble(xcord)));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("통신 메세지", "[" + response + "]"); // 서버와의 통신 결과 확인 목적
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("통신 에러", "[" + error.getMessage() + "]");
                        Log.v("통신 에러 이유",error.getStackTrace().toString());
                    }
                }) {

        };

        request.setShouldCache(false); // 이전 결과가 있더라도 새로 요청해서 응답을 보여줌
        AppHelper.requestqueue.add(request); // request queue 에 request 객체를 넣어준다.

        return null;
    }

    public static ArrayList<EarthquakeShelterData> getArrayList(){
        return arrayList;
    }
}