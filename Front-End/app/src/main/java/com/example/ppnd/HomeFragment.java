package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    Button btn_earthquake, btn_typoon, btn_thunder, btn_heatwave, btn_rain, btn_snow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btn_earthquake = (Button) getView().findViewById(R.id.btn_earthquake);
        btn_typoon = (Button) getView().findViewById(R.id.btn_typoon);
        btn_thunder = (Button) getView().findViewById(R.id.btn_thunder);
        btn_heatwave = (Button) getView().findViewById(R.id.btn_heatwave);
        btn_rain = (Button) getView().findViewById(R.id.btn_rain);
        btn_snow = (Button) getView().findViewById(R.id.btn_snow);

        btn_earthquake.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","earthquake");
                startActivity(intent);
            }
        });
        btn_typoon.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","typoon");
                startActivity(intent);
            }
        });
        btn_thunder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","thunder");
                startActivity(intent);
            }
        });
        btn_heatwave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","heatwave");
                startActivity(intent);
            }
        });
        btn_rain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","rain");
                startActivity(intent);
            }
        });
        btn_snow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisastersActivity.class);
                intent.putExtra("type","snow");
                startActivity(intent);
            }
        });


    }
}