package com.example.ppnd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private TextView current_location;
    private RecyclerView recyclerview_current_location;
    private Button btn_earthquake, btn_typhoon, btn_thunder,
            btn_hitwave, btn_rain, btn_snow, btn_emergency;

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

        btn_earthquake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지진 설명, 행동요령, 대피소
            }
        });

        btn_typhoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태풍 설명, 행동요령, 대피소
            }
        });

        btn_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //낙뢰 설명, 행동요령, 대피소
            }
        });

        btn_hitwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭염 설명, 행동요령, 대피소
            }
        });

        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //호우 설명, 행동요령, 대피소
            }
        });

        btn_snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭설 설명, 행동요령, 대피소
            }
        });

        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //긴급 전화
            }
        });

    }

    private void initView() {
        current_location = getView().findViewById(R.id.current_location);
        recyclerview_current_location = getView().findViewById(R.id.recyclerview_current_location);
        btn_earthquake = getView().findViewById(R.id.btn_earthquake);
        btn_typhoon = getView().findViewById(R.id.btn_typhoon);
        btn_thunder = getView().findViewById(R.id.btn_thunder);
        btn_hitwave = getView().findViewById(R.id.btn_hitwave);
        btn_rain = getView().findViewById(R.id.btn_rain);
        btn_snow = getView().findViewById(R.id.btn_snow);
        btn_emergency = getView().findViewById(R.id.btn_emergency);
    }
}