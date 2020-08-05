package com.example.ppnd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONREQUEST_RESULT=100; // 콜백 호출시 requestcode로 넘어가는 구분자

    private Button btn_login;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SplashActivity의 인텐트를 받아서 text값을 저장
        final Intent intent = getIntent();
        final String current_address = intent.getStringExtra("current_address");
        final String current_location_newsflash = intent.getStringExtra("current_location_newsflash");
        final String nation_wide_newsflash = intent.getStringExtra("nation_wide_newsflash");
        final Bitmap satellite_image = intent.getParcelableExtra("satellite_image");

        Log.d("진입22", current_address);

        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(getApplicationContext(), NavigationBarMainActivity.class);
                    intent.putExtra("current_location_newsflash", current_location_newsflash);
                    intent.putExtra("nation_wide_newsflash", nation_wide_newsflash);
                    intent.putExtra("satellite_image", satellite_image);
                    intent.putExtra("current_address", current_address);

                    Log.d("진입222", current_address);

                    startActivity(intent);
                    finish();
                }
                else {
                    CheckPermission();
                }
            }
        });
    }

    // 퍼미션 권한 진행 함수
    private void CheckPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED)
        {
            // 사용자의 최초 퍼미션 허용을 확인         -true: 사용자 퍼미션 거부 , -false: 사용자 동의 미 필요
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE))
            {
                Toast.makeText(getApplicationContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CALL_PHONE},
                        PERMISSIONREQUEST_RESULT);
            }
            else{
                Toast.makeText(getApplicationContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CALL_PHONE},
                        PERMISSIONREQUEST_RESULT);
            }

        }else{
            // 퍼미션을 다 동의 했을 경우 다음으로 진행
            Intent intent = new Intent(getApplicationContext(), NavigationBarMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //퍼미션 요청 결과가 onRequestPermissionsResult에서 수신
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if(requestCode==PERMISSIONREQUEST_RESULT){
            if(grantResult.length > 0){
                for (int aGrantResult : grantResult) {
                    if (aGrantResult == PackageManager.PERMISSION_DENIED) {
                        // 권한이 하나라도 거부 될 시
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("사용 권한의 문제발생")
                                .setIcon(R.drawable.main_icon)
                                .setMessage("저희 서비스 사용을 위해서는 서비스의 요구권한을 필수적으로 허용해주셔야합니다.")
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("권한 설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                getApplicationContext().startActivity(intent);
                                finish();
                            }
                        }).show();
                        return;
                    }
                }
            }
        }
    }
}