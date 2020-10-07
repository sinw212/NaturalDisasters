package com.example.ppnd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ActionFragment3 extends Fragment {

    String type;
    ViewGroup rootView;

    @Override
    public  void  onCreate ( Bundle  savedInstanceState ) {
        super . onCreate (savedInstanceState);
        //RequestActivity에서 전달한 번들 저장
        try {
            Bundle bundle = getArguments();
            if(bundle != null) {
                type = bundle.getString("type");
            }

        } catch(Exception e) {
            Log.d("오류",String.valueOf(e));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(type.equals("earthquake")){
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_earthquake3,container,false);
        }
        else if(type.equals("heatwave")){
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_heatwave3,container,false);
        }
        else if(type.equals("typhoon")){
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_typhoon3,container,false);
        }
        else if (type.equals("thunder")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_thunder3,container,false);
        } else if (type.equals("rain")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rain3,container,false);
        } else if (type.equals("snow")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_snow3,container,false);
        }

        return rootView;
    }


}




