package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Adapter.LocationAdapter;
import com.example.ppnd.Data.LocationData;
import com.example.ppnd.Other.DataParsing;

import java.util.ArrayList;

public class SearchLocationActivity extends AppCompatActivity {

    private RecyclerView recyclerview = null;
    private LinearLayoutManager layoutManager = null;
    private LocationAdapter searchlocationAdapter;
    private ArrayList<LocationData> arrayList;
    private LocationData searchlocationData;

    private TextView search_location;

    private String data;
    private String search_address = null;
    private int search_code;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlocation);

        search_location = findViewById(R.id.search_location);
        recyclerview = findViewById(R.id.recyclerview_search_location);

        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        searchlocationAdapter = new LocationAdapter(arrayList);
        recyclerview.setAdapter(searchlocationAdapter);

        //NewsFlashFragment 에서 넘긴 값 받아오기
        Intent intent = getIntent();
        search_address = intent.getStringExtra("current_address");
        search_code = intent.getIntExtra("current_code", 0);

        search_location.setText(search_address + " 속보");

        //XML 파싱해서 Adapter에 연결하는 과정
        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        new Thread(new Runnable() {
            @Override
            public void run() {
                //검색한 위치 속보 받아오기
                data = DataParsing.newsflashXmlData(search_code); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String split_data[] = data.split("\n");
                        for(int i=0; i<split_data.length; i++) {
                            searchlocationData = new LocationData(split_data[i]);

                            arrayList.add(searchlocationData); // RecyclerView의 마지막 줄에 삽입
                            searchlocationAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }
}