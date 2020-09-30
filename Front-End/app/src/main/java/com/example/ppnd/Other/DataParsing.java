package com.example.ppnd.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataParsing extends AppCompatActivity {

    public static String serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D";

    public static SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd");
//    public static SimpleDateFormat format_time = new SimpleDateFormat("HHmm");
    public static String date, time;

    public static StringBuffer buffer;
    public static Bitmap bm;

    //기상청 속보 받아오기
    public static String newsflashXmlData (int current_code) {
        Log.d("진입속보", "ㅇㅇ");
        buffer = new StringBuffer();

        //오늘 날짜 받아오기 (속보 받아올 때 사용)
        date = format_date.format(new Date());

        try {
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg?serviceKey=" +
                    serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&stnId="+current_code+"&fromTmFc=20200924&toTmFc="+date+"&");

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
        Log.d("진입buffer", String.valueOf(buffer));
        return buffer.toString();
    }

    public static Bitmap satelliteXmlData() {
        try {
            URL url = new URL("http://www.weather.go.kr/repositary/image/sat/gk2a/KO/gk2a_ami_le1b_ir105_ko020lc_202009300004.thn.png");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); //서버로부터 응답 수신
            conn.connect();

            InputStream is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(is); //Bitmap으로 변환
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bm;
    }
}