package com.example.ppnd.Data;

import java.io.Serializable;

public class EarthquakeShelterData implements Serializable {
    String location;
    Double lat, lng;

    public EarthquakeShelterData(String location, Double lat, Double lng){
        this.location = location;
        this.lat = lat;
        this.lng = lng;
    }

    public String getLocation(){
        return location;
    }

    public Double getLat(){
        return lat;
    }

    public Double getLng(){
        return lng;
    }
}
