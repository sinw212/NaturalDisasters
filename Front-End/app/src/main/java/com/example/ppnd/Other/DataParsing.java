package com.example.ppnd.Other;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataParsing {

    private static String serviceKey;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private static String date;

    private static StringBuffer buffer;

    //기상청 속보
    public static String newsflashXmlData (int current_code) {

        serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D";
        buffer = new StringBuffer();

        //오늘 날짜 받아오기
        date = format.format(new Date());

        try {
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg?serviceKey=" +
                    serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&stnId="+current_code+"&fromTmFc="+date+"&toTmFc="+date+"&");

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

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
                        if(parser.getName().equals("item")) {
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}