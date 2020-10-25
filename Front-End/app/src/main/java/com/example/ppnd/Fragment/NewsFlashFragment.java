package com.example.ppnd.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Adapter.LocationAdapter;
import com.example.ppnd.Data.LocationData;
import com.example.ppnd.Other.LocationCode;
import com.example.ppnd.R;
import com.example.ppnd.SearchLocationActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class NewsFlashFragment extends Fragment {
    private Context mContext;
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

        nationwideAdapter = new LocationAdapter(mContext, arrayList);
        recyclerview.setAdapter(nationwideAdapter);

        //MainActivity에서 전달한 번들 저장
        Bundle bundle = getArguments();
        String newsflash_data = bundle.getString("nation_wide_newsflash"); //전국 속보 받아오기 (전국코드 : 108)
        byte[] bm = bundle.getByteArray("satellite_image"); //위성사진 받아오기
        Bitmap satellite_data = BitmapFactory.decodeByteArray(bm,0,bm.length);

        String[] split_data = newsflash_data.split("\n");
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

            nationwideData = new LocationData(ssplit_data.get(i));

            arrayList.add(nationwideData); // RecyclerView의 마지막 줄에 삽입
            nationwideAdapter.notifyDataSetChanged();
        }
        photoView_satellite.setImageBitmap(satellite_data); //이미지뷰에 위성사진 띄우기

        //검색 버튼 리스너
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_address = LocationCode.currentAddress(String.valueOf(et_search.getText()));
                current_code = LocationCode.currentLocationCode(current_address);

                if(current_address.equals("null")) {
                    Toast.makeText(getContext(), "잘못 입력하였습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
                    intent.putExtra("current_address", current_address);
                    intent.putExtra("current_code", current_code);
                    startActivity(intent);
                }
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