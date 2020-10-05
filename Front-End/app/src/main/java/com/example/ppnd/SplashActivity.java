package com.example.ppnd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.ppnd.Other.EarthquakeShelterTask;
import com.example.ppnd.Other.HeatWaveParsing;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashActivity extends Activity {

    HeatWaveParsing heatWaveParsing;
    EarthquakeShelterTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        // 무더위 대피소 api
        // 지역코드 변환

        String Local1 = "강원도";
        String Local2 = "원주시";

       /* if(Local2.equals("창원시"))// 창원시는 창원시 ㅇㅇ구로 표현되기 때문에 따로 검사
            Local2 += " "+arr_area[3];
        else{// 수원시 ~ 전주시는 ㅇㅇ시ㅇㅇ구로 표현되기 때문에 따로 검사
            String city[] = {"수원시","성남시","안양시","안산시","고양시","용인시","청주시","천안시","전주시"};
            int size = city.length;
            for(int i =0; i<size; i++){
                if(Local2.equals(city[i]))
                    Local2 += arr_area[3];
            }
        }*/

        // 지역코드 csv 파일에서 받아오기
        ArrayList list = new ArrayList<>();

        try
        {
            InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.areacode));
            BufferedReader reader = new BufferedReader(is);
            CSVReader read = new CSVReader(reader);
            String record = null;
            boolean flag = false;
            while ((record = reader.readLine()) != null){
                String arr[] = record.split("/");
                // Log.d("레코드",record);

                if(flag){
                    if(arr.length < 4)
                        break;
                    else {
                        if (!arr[2].equals(Local2))
                            break;
                    }
                }
                if(arr.length == 4) {
                    if (arr[1].equals(Local1)) {
                        if (arr[2].equals(Local2)) {
                            {
                                if (arr[3].equals("태장2동")) {
                                    list.add(arr[0]);
                                    flag = true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            task = new EarthquakeShelterTask();
            task.execute();
            heatWaveParsing = new HeatWaveParsing(list,this);
            heatWaveParsing.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, NavigationBarMainActivity.class);
        startActivity(intent);
    }

}