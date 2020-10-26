package com.example.ppnd.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ppnd.R;

public class ActionFragment4 extends Fragment {

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
        if (type.equals("thunder")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_thunder4,container,false);
        } else if (type.equals("rain")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rain4,container,false);
        } else if (type.equals("snow")) {
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_snow4,container,false);
        }

        return rootView;
    }


}




