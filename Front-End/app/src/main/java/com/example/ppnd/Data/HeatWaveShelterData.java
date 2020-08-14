package com.example.ppnd.Data;

public class HeatWaveShelterData {
    String address;
    String loc_nm;

    public HeatWaveShelterData(String address, String loc_nm){
        this.address = address;
        this.loc_nm = loc_nm;
    }

    public String getAddress(){
        return address;
    }

    public String getLoc_nm(){
        return loc_nm;
    }
}
