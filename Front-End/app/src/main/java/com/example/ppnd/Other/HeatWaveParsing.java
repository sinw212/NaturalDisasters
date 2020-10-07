package com.example.ppnd.Other;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ppnd.Data.HeatWaveShelterData;
import com.example.ppnd.R;
import com.opencsv.CSVReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HeatWaveParsing extends AsyncTask<String, Void, String> {
    String serviceKey_Decoder="";
    StringBuilder urlBuilder;
    private static ArrayList<HeatWaveShelterData> address = new ArrayList();
    String serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D"; // 서비스 키
    ArrayList areaCode = new ArrayList<>();
    String[] equptype = {"001","002","003","004","005","006","007","008","009","010","099",};
    Context mcontext;

    public HeatWaveParsing(ArrayList areaCode, Context context){
        this.mcontext = context;
        this.areaCode = areaCode;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("파싱2","실행");

        try {
            serviceKey_Decoder = URLDecoder.decode(serviceKey.toString(), "UTF-8");
            Log.v("서비스 키 ", serviceKey_Decoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int size = areaCode.size();
        for(int i =0; i <size;i++) {
            for(int j =0; j <equptype.length; j++) {
                urlBuilder = new StringBuilder("http://apis.data.go.kr/1750000/heatwaveShelterService/RegionalShelterTypeCrntSt"); /*URL*/
                try {
                    urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey_Decoder, "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode("2018", "UTF-8")); /*연도*/
                    urlBuilder.append("&" + URLEncoder.encode("areaCd", "UTF-8") + "=" + URLEncoder.encode(areaCode.get(i).toString(), "UTF-8")); /*지역코드*/
                    urlBuilder.append("&" + URLEncoder.encode("equpType", "UTF-8") + "=" + URLEncoder.encode(equptype[j], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //try를 통해 요청을 시작합니다.
                try {
                    boolean item = false;
                    boolean restaddr = false;
                    boolean restname = false;

                    URL url = new URL(urlBuilder.toString());
                    InputStream is = url.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new InputStreamReader(is, "UTF-8"));

                    String temp_restaddr = "";
                    String temp_restname = "";

                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        //Log.d("findpath_parser", String.valueOf(eventType)+" name : "+parser.getName()+" text : "+parser.getText());
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;

                            case XmlPullParser.START_TAG: {
                                if (parser.getName().equals("item")) {
                                    item = true;
                                    Log.d("findpath--------", "--------");
                                }

                                if (parser.getName().equals("restaddr")) {
                                    Log.d("restaddr", "--------");
                                    restaddr = true;
                                }

                                if (parser.getName().equals("restname")) {
                                    restname = true;
                                }
                                break;
                            }

                            case XmlPullParser.TEXT: {
                                if (item) {
                                    item = false;
                                }
                                if (restaddr) {
                                    temp_restaddr = parser.getText();
                                    Log.d("temp_restaddr", temp_restaddr);
                                    restaddr = false;
                                }
                                if (restname) {
                                    temp_restname = parser.getText();
                                    Log.d("temp_restname", temp_restname);
                                    address.add(new HeatWaveShelterData(mcontext,temp_restaddr,temp_restname));
                                    restname = false;
                                }
                                break;
                            }
                            case XmlPullParser.END_TAG:
                                if (parser.getName().equals("item")) {
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

        }

        return null;
    }


    public static ArrayList<HeatWaveShelterData> getArrayList(){
        return address;
    }


}