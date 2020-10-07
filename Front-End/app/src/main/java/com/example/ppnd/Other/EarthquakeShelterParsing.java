package com.example.ppnd.Other;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.ppnd.Data.EarthquakeShelterData;
import com.example.ppnd.Data.HeatWaveShelterData;
import com.example.ppnd.R;
import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EarthquakeShelterParsing extends AsyncTask<String, Void, String> {
    private static ArrayList<EarthquakeShelterData> arrayList = new ArrayList<>();
    String serviceKey_Decoder="";
    StringBuilder urlBuilder;
    String serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D"; // 서비스 키

    String local1 = "";
    String local2 = "";

    public EarthquakeShelterParsing(String local1, String local2){
        this.local1 = local1;
        this.local2 = local2;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            serviceKey_Decoder = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            Log.v("서비스 키 ", serviceKey_Decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            serviceKey_Decoder  = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            // Log.v("서비스 키 ", serviceKey_Decoder);



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for(int i =1; i <= 5; i++) {
            urlBuilder = new StringBuilder("http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList"); //*URL*//*
            try {
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(serviceKey_Decoder,"UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("flag","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //try를 통해 요청을 시작합니다.
            try {
                boolean row = false;
                boolean xcord = false;
                boolean ycord = false;
                boolean location = false;
                boolean ctprvn_nm = false;
                boolean sgg_nm = false;

                URL url = new URL(urlBuilder.toString());
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String temp_xcord="";
                String temp_ycord="";
                String temp_location = "";
                String temp_ctprvn_nm = "";
                String temp_sgg_nm ="";

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //Log.d("findpath_parser", String.valueOf(eventType)+" name : "+parser.getName()+" text : "+parser.getText());
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG: {
                            if (parser.getName().equals("row")) {
                                row = true;
                            }
                            if (parser.getName().equals("ctprvn_nm")) {
                                ctprvn_nm = true;
                            }
                            if (parser.getName().equals("sgg_nm")) {
                                sgg_nm = true;
                            }
                            if (parser.getName().equals("vt_acmdfclty_nm")) {
                                location = true;
                            }
                            if (parser.getName().equals("xcord")) {
                                xcord = true;
                            }

                            if (parser.getName().equals("ycord")) {
                                ycord = true;
                            }

                            break;
                        }

                        case XmlPullParser.TEXT: {
                            if (row) {
                                row = false;
                            }
                            if (ctprvn_nm) {
                                temp_ctprvn_nm = parser.getText();
                                ctprvn_nm = false;
                            }
                            if (sgg_nm) {
                                temp_sgg_nm = parser.getText();
                                sgg_nm = false;
                            }
                            if (location) {
                                temp_location = parser.getText();
                                location = false;
                            }
                            if (xcord) {
                                temp_xcord = parser.getText();
                                xcord = false;
                            }
                            if (ycord) {
                                temp_ycord = parser.getText();
                                if(temp_ctprvn_nm.equals(local1)) {
                                    if (temp_sgg_nm.equals(local2)) {
                                        Log.d("지진", temp_location);
                                        arrayList.add(new EarthquakeShelterData(temp_location, Double.parseDouble(temp_ycord), Double.parseDouble(temp_xcord)));
                                    }
                                }
                                ycord = false;
                            }

                            break;
                        }
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("row")) {
                                break;
                            }

                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("HomeFra_transportapi_", "Error: " + e.getMessage());
            }
        }

            return null;
    }


    public static ArrayList<EarthquakeShelterData> getArrayList(){
        return arrayList;
    }
}
