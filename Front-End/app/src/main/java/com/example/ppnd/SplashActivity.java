package com.example.ppnd;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch(InterruptedException e) {
            System.err.println("SplashActivity InterruptedException error");
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED)) {
            // 마시멜로우 이상 부터 권한 체크 진행
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); // 권한 미허용으로 인해 '권한 허용' 액티비티 연결
            finish();
        }
        else{
            startActivity(new Intent(getApplicationContext(),MainActivity.class)); // 모든 권한 허용으로 인해 MainActivity 연결
            finish(); // 해당 액티비티 종료
        }
    }
}