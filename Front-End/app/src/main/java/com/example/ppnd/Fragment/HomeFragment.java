package com.example.ppnd.Fragment;

import android.content.Context;
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
import java.util.List;

public class HomeFragment extends Fragment {
    private Context mContext;
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

        currentlocationAdapter = new LocationAdapter(mContext, arrayList);
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
//                Intent Call = new Intent(Intent.ACTION_CALL);
//                Call.setData(Uri.parse("tel:010-9173-8332"));
//                startActivity(Call);
            }
        });

        //MainActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        current_address = bundle.getString("current_address"); //GPS기준 현재 위치 주소
        data = bundle.getString("current_location_newsflash"); //GPS기준 현재 위치 속보

        current_location.setText(current_address + " 속보");

        String[] split_data = data.split("\n");
        ArrayList<String> ssplit_data = new ArrayList<>();
        int size = split_data.length;

        for(int i=0; i<size; i++) {
            if(split_data[i].equals("현재 속보가 존재하지 않습니다.") ||
                    split_data[i].equals("오류가 발생했습니다. 다시 시도해주세요."))
                ssplit_data.add(split_data[i]);
            else {
                if(split_data[i].substring(0,1).equals("(")) {
                    split_data[i] = split_data[i].substring(3, split_data[i].length());
                    ssplit_data.add(split_data[i] + "\n");
                }
                else
                    ssplit_data.set(ssplit_data.size()-1,ssplit_data.get(ssplit_data.size()-1)+split_data[i]+"\n");
            }
        }

        int ssize = ssplit_data.size();
        for(int i = 0; i < ssize; i++) {
            Log.d("진입1", ssplit_data.get(i));

            currentlocationData = new LocationData(ssplit_data.get(i));

            arrayList.add(currentlocationData); // RecyclerView의 마지막 줄에 삽입
            currentlocationAdapter.notifyDataSetChanged();
        }
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