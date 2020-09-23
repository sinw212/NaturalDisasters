package com.example.ppnd.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Adapter.LocationAdapter;
import com.example.ppnd.Data.LocationData;
import com.example.ppnd.Other.DataParsing;
import com.example.ppnd.Other.LocationCode;
import com.example.ppnd.R;
import com.example.ppnd.SearchLocationActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class NewsFlashFragment extends Fragment {

    private RecyclerView recyclerview = null;
    private LinearLayoutManager layoutManager;
    private LocationAdapter nationwideAdapter = null;
    private ArrayList<LocationData> arrayList;
    private LocationData nationwideData;

    private EditText et_search;
    private Button btn_search;
    private PhotoView photoView_satellite;

    private String current_address;
    private int current_code;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newsflash, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        nationwideAdapter = new LocationAdapter(arrayList);
        recyclerview.setAdapter(nationwideAdapter);

        //MainActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        Log.d("진입9999", "ㅇㅇ");
        String newsflash_data = bundle.getString("nation_wide_newsflash"); //전국 속보 받아오기 (전국코드 : 108)
        Log.d("진입1010", newsflash_data);
        Bitmap satellite_data = bundle.getParcelable("satellite_image"); //위성사진 받아오기

        Log.d("진입전국속보", "ㅇㅇㅇㅇㅇ");
        String []split_data = newsflash_data.split("\n");
        int size = split_data.length;
        for(int i=0; i<size; i++) {
            nationwideData = new LocationData(split_data[i]);

            arrayList.add(nationwideData); //RecyclerView의 마지막 줄에 삽입
            nationwideAdapter.notifyDataSetChanged();
        }
        Log.d("진입위성사진", "ㅇㅇ");
        photoView_satellite.setImageBitmap(satellite_data); //이미지뷰에 위성사진 띄우기

        /*
        //XML 파싱해서 Adapter에 연결하는 과정
        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        new Thread(new Runnable() {
           @Override
            public void run() {
                //전국 속보 받아오기 (전국 코드 : 108)
                newsflash_data = DataParsing.newsflashXmlData(108); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("진입위성사진", "ㅇㅇ");
                        photoView_satellite.setImageBitmap(DataParsing.satelliteXmlData()); //이미지 뷰에 위성사진 띄우기

                        Log.d("진입전국속보", "ㅇㅇ");
                        String split_data[] = newsflash_data.split("\n");
                        for(int i=0; i<split_data.length; i++) {
                            nationwideData = new LocationData(split_data[i]);

                            arrayList.add(nationwideData); // RecyclerView의 마지막 줄에 삽입
                            nationwideAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
        */

        //검색 버튼 리스너
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_address = LocationCode.currentAddress(String.valueOf(et_search.getText()));
                current_code = LocationCode.currentLocationCode(current_address);

                Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
                intent.putExtra("current_address", current_address);
                intent.putExtra("current_code", current_code);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        et_search = getView().findViewById(R.id.et_search);
        btn_search = getView().findViewById(R.id.btn_search);
        recyclerview = getView().findViewById(R.id.recyclerview_nationwide_location);
        photoView_satellite = getView().findViewById(R.id.photoView_satellite);
    }
}