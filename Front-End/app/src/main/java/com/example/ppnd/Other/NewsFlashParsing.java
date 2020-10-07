package com.example.ppnd.Other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewsFlashParsing extends AppCompatActivity {

    public static String serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D";

    public static SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat format_date_time = new SimpleDateFormat("yyyyMMddHHmm");
    public static String date, date_time;
    public static Calendar cal = Calendar.getInstance();

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
                    serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&stnId="+current_code+"&fromTmFc=20201004&toTmFc="+date+"&");

//            http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg?serviceKey=ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D&pageNo=1&numOfRows=10&dataType=XML&stnId=108&fromTmFc=20200927&toTmFc=20201003&

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while(parserEvent != XmlPullParser.END_DOCUMENT) {
                switch(parserEvent) {
                    case XmlPullParser.START_DOCUMENT: //XML문서가 시작되면 실행
                        break;
                    case XmlPullParser.START_TAG: //시작 태그를 만나면 실행 (ex. <data>)
                        if(parser.getName().equals("resultCode")) {
                            parser.next();
                            if (parser.getText().equals("00")) {}//정상 코드
                            else if (parser.getText().equals("03"))//NoData 코드
                                buffer.append("현재 속보가 존재하지 않습니다.\n");
                            else//그 외 오류 발생 코드
                                buffer.append("오류가 발생했습니다. 다시 시도해주세요.\n");
                        }
                        else if (parser.getName().equals("t2")) { //속보 발생 해당구역
                            parser.next();
                            buffer.append(parser.getText());//t2 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;
                    case XmlPullParser.END_TAG: //끝 태그를 만나면 실행 (ex. </data>)
                        break;
                    case XmlPullParser.TEXT: //태그의 시작과 끝 사이에서 실행 (ex. <data>"이 부분"</data>)
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

        //오늘 날짜+시간 받아오기 (위성사진 받아올 때 사용)
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, -9); //UTC기준(한국 표준시 -9시간)
        date_time = format_date_time.format(cal.getTime());

        //위성사진이 2분 단위로 제공됨
        if(Double.parseDouble(date_time)%2 == 0)
            cal.add(Calendar.MINUTE, -8);
        else
            cal.add(Calendar.MINUTE, -7);

        date_time = format_date_time.format(cal.getTime());
        Log.d("진입dat_time", date_time);


        try {
            URL url = new URL("http://www.weather.go.kr/repositary/image/sat/gk2a/KO/gk2a_ami_le1b_ir105_ko020lc_" + date_time + ".thn.png");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); //서버로부터 응답 수신
            conn.connect();

            InputStream is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(is); //Bitmap으로 변환
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bm;
    }
}