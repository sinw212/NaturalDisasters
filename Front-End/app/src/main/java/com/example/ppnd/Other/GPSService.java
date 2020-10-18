package com.example.ppnd.Other;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class GPSService extends Service implements LocationListener {

    private Context mContext;
    private static Location location;
    private static double latitude;
    private static double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; //1분
    protected LocationManager locationManager = null;

    public GPSService(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try {
            Log.d("Check", "getLocation");
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(locationManager != null) {
                // NULL POINT EXCEPTION 제거
                if (!isGPSEnabled && !isNetworkEnabled) {
                    Log.d("Check", "1");
//                    new AlertDialog.Builder(mContext)
//                            .setCancelable(false)
//                            .setTitle("GPS 미설정")
//                            .setMessage("GPS가 미설정 되어 서비스 제공에 어려움이 있으니 GPS 사용을 허용해주세요\n" +
//                                    "허용한 뒤, 원활한 사용을 위해 앱을 다시 켜주십시오.")
//                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    setting.addCategory(Intent.CATEGORY_DEFAULT);
//                                    startActivity(setting);
//                                    mContext.finish();
//                                }
//                            }).show();
                } else {
                    Log.d("Check", "2");
                    int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Check", "3");
                    } else
                        return null;

                    if (isNetworkEnabled) {
                        Log.d("Check", "4");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            Log.d("Check", "5");
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                Log.d("Check", "6");
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    if (isGPSEnabled) {
                        Log.d("Check", "7");
                        if (location == null) {
                            Log.d("Check", "8");
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                Log.d("Check", "9");
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    Log.d("Check", "10");
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
            else // 에러 알림
                Toast.makeText(mContext,"GPS NULL ERROR", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Check", ""+e.toString());
        }
        return location;
    }

    public static double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public static double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSService.this);
        }
    }
}