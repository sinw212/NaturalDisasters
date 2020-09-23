package com.example.ppnd.Other;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.MainActivity;
import com.example.ppnd.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GPSService extends Service implements LocationListener {

    public static Intent serviceIntent = null;
    private Location location;
    private double latitude;
    private double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;    //  1분
    protected LocationManager locationManager = null;

    private static RequestQueue requestQueue;

    private String full_address;
    public static String current_address;
    public static int current_code;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        getLocation();
//        initializeNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    /*
    public void initializeNotification() {

        PendingIntent pendingIntent = null;
        PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);
    }
    */

    //GPS값은 자원을 안쓰면 반드시 해제해줘야함
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, ReStartReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        locationManager.removeUpdates(this);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, ReStartReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    public Location getLocation() {
        try {
            Log.d("Check", "getLocation");
            Context mContext = getApplicationContext();
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("Check", "1");
            } else {
                Log.d("Check", "2");
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Check", "3");
                } else {
                    return null;
                }

                if (isNetworkEnabled) {
                    Log.d("Check", "4");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
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
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
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
            Log.d("진입getLocation", "dd");
            addressCode(location.getLatitude(), location.getLongitude());
            Log.d("Check", "lati: " + latitude + ", longi: " + longitude);
        } catch (Exception e) {
            Log.d("Check", "" + e.toString());
        }
        return location;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Check", "Call onLocationChanged");
        Log.d("진입onLocationChanged", "dd");
        addressCode(location.getLatitude(), location.getLongitude());
    }

    private String addressCode(double lati, double longi) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocation(lati, longi, 7);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addressList == null || addressList.size() ==0) {
            return "주소 미발견";
        }
        full_address = addressList.get(0).getAddressLine(0); //위경도 값을 주소로 변환한 전체 주소
        Log.d("진입full_address", full_address);
        current_address = LocationCode.currentAddress(full_address);
        Log.d("진입current_address", current_address);
        current_code = LocationCode.currentLocationCode(current_address);
        Log.d("진입current_code", String.valueOf(current_code));

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        return "tt";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}