package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.Data.EarthquakeShelterData;
import com.example.ppnd.Data.HeatWaveShelterData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        Intent intent = getActivity().getIntent();
        final ArrayList<EarthquakeShelterData> list1 = (ArrayList<EarthquakeShelterData>) intent.getSerializableExtra("earthquakeshelter");
        final ArrayList<HeatWaveShelterData> list2 = (ArrayList<HeatWaveShelterData>) intent.getSerializableExtra("heatwaveshelter");


        btn_earthquake = (Button) getView().findViewById(R.id.btn_earthquake);
        btn_typoon = (Button) getView().findViewById(R.id.btn_typoon);
        btn_thunder = (Button) getView().findViewById(R.id.btn_thunder);
        btn_heatwave = (Button) getView().findViewById(R.id.btn_heatwave);
        btn_rain = (Button) getView().findViewById(R.id.btn_rain);
        btn_snow = (Button) getView().findViewById(R.id.btn_snow);

        btn_typoon.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "typoon");
                startActivity(intent);
            }
        });
        btn_thunder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "thunder");
                startActivity(intent);
            }
        });
        btn_rain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "rain");
                startActivity(intent);
            }
        });
        btn_snow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters1Activity.class);
                intent.putExtra("type", "snow");
                startActivity(intent);
            }
        });
        btn_earthquake.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters2Activity.class);
                intent.putExtra("type", "earthquake");
                intent.putExtra("earthquakeshelter", list1);
                startActivity(intent);
            }
        });
        btn_heatwave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NaturalDisasters2Activity.class);
                intent.putExtra("type", "heatwave");
                intent.putExtra("heatwaveshelter", list2);
                startActivity(intent);
            }
        });

    }

}