package com.example.ppnd.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataParsing extends AppCompatActivity {

    public static String serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D";

    public static SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd");
//    public static SimpleDateFormat format_time = new SimpleDateFormat("HHmm");
    public static String date, time;

    public static StringBuffer buffer;
    public static Bitmap bm;

    //기상청 속보 받아오기
    public static String newsflashXmlData (int current_code) {
        buffer = new StringBuffer();

        //오늘 날짜 받아오기 (속보 받아올 때 사용)
        date = format_date.format(new Date());

        try {
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg?serviceKey=" +
                    serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&stnId="+current_code+"&fromTmFc="+date+"&toTmFc="+date+"&");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            //NoData일때 if문 처리해주기
           while(parserEvent != XmlPullParser.END_DOCUMENT) {
                switch(parserEvent) {
                    case XmlPullParser.START_TAG: //parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("t2")) { //속보 발생 해당구역
                            parser.next();
                            buffer.append(parser.getText());//t2 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;
                    case XmlPullParser.TEXT: //parser가 내용에 접근했을 때
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")) { }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public Bitmap satelliteXmlData() {
        try {
            URL url = new URL("http://www.weather.go.kr/repositary/image/sat/gk2a/KO/gk2a_ami_le1b_ir105_ko020lc_202008041646.thn.png");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (Exception e) { }

        return bm;
    }

//    //한반도 위성 사진 받아오기
//    public Bitmap satelliteXmlData() {
//        String url ="http://www.weather.go.kr/repositary/image/sat/gk2a/KO/gk2a_ami_le1b_ir105_ko020lc_202008041646.thn.png";
//        Log.d("진입1",url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        bm = StringToBitmap(response);
//                        Log.d("진입2",bm.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                return params;
//            }
//        };
//
//        stringRequest.setShouldCache(false); // Volley 자체는 캐싱을 하기때문에, 캐싱기능을 꺼버림
//        VolleyQueueSingleTon.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//        Log.d("진입3",bm.toString());
//        return bm;
//    }
//
//    //String을 Bitmap으로 변환
//    private static Bitmap StringToBitmap(String encodedString) {
//        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        return bitmap;
//    }
}