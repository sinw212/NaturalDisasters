package com.example.ppnd;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ShelterActivity extends AppCompatActivity {
    StringBuilder urlBuilder;
    TextView result;
    String serviceKey = "";
    String serviceKey_Decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);

        // request queue 는 앱이 시작되었을 때 한 번 초기화되기만 하면 계속 사용이 가능
        if(AppHelper.requestqueue == null)
            AppHelper.requestqueue = Volley.newRequestQueue(this);

        result = (TextView) findViewById(R.id.result);

        try {
            serviceKey_Decoder  = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            Log.v("서비스 키 ", serviceKey_Decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        urlBuilder = new StringBuilder("https://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList"); //*URL*//*

        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(serviceKey_Decoder,"UTF-8"));
            // urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("MMTvvs9CkQ5sr%2FEsNtaRf6VgieVpIVCoeNNPtEvPJhc0MfjRXxulb3OLUtK%2BBnBPjXJP4hjokBNt1bOUJg74jw%3D%3D", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("flag","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        result.setText(earthquakeRequest());

    }

    private String earthquakeRequest() {
        final StringBuffer buffer=new StringBuffer();
        URI uri = null;
        try {
            uri = new URI(String.valueOf(urlBuilder));
            Log.v("uri 변환",uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, uri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        Log.v("결과",response);
                        try {

                            JSONArray jarray = new JSONObject(response).getJSONArray("row");

                            int size = jarray.length();
                            for (int i = 1; i < size; i++) {
                                JSONObject jsonObject = jarray.getJSONObject(i);
                                String xcord = jsonObject.optString("xcord");
                                String ycord = jsonObject.optString("ycord");
                                buffer.append(xcord);
                                buffer.append(ycord);
                                buffer.append("\n");

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
                        Log.v("통신 에러 이유",error.getStackTrace().toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("ServiceKey","MMTvvs9CkQ5sr%2FEsNtaRf6VgieVpIVCoeNNPtEvPJhc0MfjRXxulb3OLUtK%2BBnBPjXJP4hjokBNt1bOUJg74jw%3D%3D");
                params.put("pageNo","1");
                params.put("numOfRows","10");
                params.put("type","json");
                params.put("flag","Y");*/
                return params;

            }
        };

        // 캐시 데이터 가져오지 않음 왜냐면 기존 데이터 가져올 수 있기때문
        // 항상 새로운 데이터를 위해 false
        stringRequest.setShouldCache(false);
        AppHelper.requestqueue.add(stringRequest);

        return buffer.toString();
    }
}


