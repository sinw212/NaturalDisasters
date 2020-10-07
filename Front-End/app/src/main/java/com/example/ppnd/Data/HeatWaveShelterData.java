package com.example.ppnd.Data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class HeatWaveShelterData {
    Double lat, lng;
    String loc_nm;

    public HeatWaveShelterData(Context context, String adr, String loc_nm){
        this.loc_nm = loc_nm;
        Geocoder geocoder = new Geocoder(context);

        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(
                    adr,
                    10); // 얻어올 값의 개수
            if(address!= null){
                lat = address.get(0).getLatitude();
                lng = address.get(0).getLongitude();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }

    }

    public String getLocation(){
        return loc_nm;
    }

    public Double getLat(){
        return lat;
    }

    public Double getLng(){
        return lng;
    }
}
