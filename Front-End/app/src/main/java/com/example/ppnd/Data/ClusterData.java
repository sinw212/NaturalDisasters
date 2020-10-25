package com.example.ppnd.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterData implements ClusterItem {
    private  LatLng mPosition;
    private String title;

    public ClusterData(Double lat, Double lng, String title){
        mPosition = new LatLng(lat, lng);
        this.title = title;
    }
    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    public String gettitle(){
        return title;
    }
}
