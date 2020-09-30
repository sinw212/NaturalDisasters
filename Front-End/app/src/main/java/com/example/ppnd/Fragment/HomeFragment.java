package com.example.ppnd.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Adapter.LocationAdapter;
import com.example.ppnd.Data.LocationData;
import com.example.ppnd.NaturalDisasters1Activity;
import com.example.ppnd.NaturalDisasters2Activity;
import com.example.ppnd.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerview = null;
    private LinearLayoutManager layoutManager = null;
    private LocationAdapter currentlocationAdapter;
    private ArrayList<LocationData> arrayList;
    private LocationData currentlocationData;

    private TextView current_location;
    private Button btn_earthquake, btn_typhoon, btn_thunder,
            btn_heatwave, btn_rain, btn_snow, btn_emergency;

    private String data;
    private String current_address;

    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        currentlocationAdapter = new LocationAdapter(arrayList);
        recyclerview.setAdapter(currentlocationAdapter);

        //지진 버튼
        btn_earthquake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지진 설명, 행동요령, 대피소
                intent = new Intent(getActivity(), NaturalDisasters2Activity.class);
                intent.putExtra("type", "earthquake");
                startActivity(intent);
            }
        });

        //태풍 버튼
        btn_typhoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태풍 설명, 행동요령
                intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "typhoon");
                startActivity(intent);
            }
        });

        //낙뢰 버튼
        btn_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //낙뢰 설명, 행동요령
                intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "thunder");
                startActivity(intent);
            }
        });

        //폭염 버튼
        btn_heatwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭염 설명, 행동요령, 대피소
                intent = new Intent(getActivity(), NaturalDisasters2Activity.class);
                intent.putExtra("type", "heatwave");
                startActivity(intent);
            }
        });

        //호우 버튼
        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //호우 설명, 행동요령
                intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "rain");
                startActivity(intent);
            }
        });

        //폭설 버튼
        btn_snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭설 설명, 행동요령
                intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "snow");
                startActivity(intent);
            }
        });

        //119 신고버튼 (GPS 기능 필요없음)
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Call = new Intent(Intent.ACTION_CALL);
                Call.setData(Uri.parse("tel:010-9173-8332"));
                startActivity(Call);
            }
        });

        //MainActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        current_address = bundle.getString("current_address"); //GPS기준 현재 위치 주소
        data = bundle.getString("current_location_newsflash"); //GPS기준 현재 위치 속보

        current_location.setText(current_address + " 속보");

        String[] split_data = data.split("\n");
        int size = split_data.length;
        for (int i = 0; i < size; i++) {
            currentlocationData = new LocationData(split_data[i]);

            arrayList.add(currentlocationData); // RecyclerView의 마지막 줄에 삽입
            currentlocationAdapter.notifyDataSetChanged();
        }

        /*
        //XML 파싱해서 Adapter에 연결하는 과정
        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        new Thread(new Runnable() {
            @Override
            public void run() {
                //GPS기준 현재 위치 속보 받아오기
                data = DataParsing.newsflashXmlData(current_code); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String split_data[] = data.split("\n");
                        for(int i=0; i<split_data.length; i++) {
                            currentlocationData = new LocationData(split_data[i]);

                            arrayList.add(currentlocationData); // RecyclerView의 마지막 줄에 삽입
                            currentlocationAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
        */
    }

    private void initView() {
        current_location = getView().findViewById(R.id.current_location);
        recyclerview = getView().findViewById(R.id.recyclerview_current_location);
        btn_earthquake = getView().findViewById(R.id.btn_earthquake); //지진 버튼
        btn_typhoon = getView().findViewById(R.id.btn_typhoon); //태풍 버튼
        btn_thunder = getView().findViewById(R.id.btn_thunder); //낙뢰 버튼
        btn_heatwave = getView().findViewById(R.id.btn_heatwave); //폭염 버튼
        btn_rain = getView().findViewById(R.id.btn_rain); //호우 버튼
        btn_snow = getView().findViewById(R.id.btn_snow); //폭설 버튼
        btn_emergency = getView().findViewById(R.id.btn_emergency); //119 신고 버튼
    }
}