package com.example.ppnd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    // GPS
    final int REQUEST_CODE_LOCATION = 2;
    private LocationManager locationManagerGPS;

    private TextView current_location;
    private RecyclerView recyclerview_current_location;
    private Button btn_earthquake, btn_typhoon, btn_thunder,
            btn_hitwave, btn_rain, btn_snow, btn_emergency;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        //GPS 미 설정시 false, 설정 시 true
        if (!locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS 설정 off일 시 설정화면으로 이동
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle("GPS 미설정 알림")
                    .setIcon(R.drawable.main_icon)
                    .setMessage("GPS가 미설정 되어있어, 위급 시 속보 알림을 받지 못할 우려가 있으니 GPS 사용을 허용해주세요\n" +
                            "허용한 후, 원활한 사용을 위해 앱을 다시 켜주십시오.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            setting.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(setting);
                            getActivity().finish();
                        }
                    }).show();
        }

        //119 신고버튼 (GPS 기능 필요없음)
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Call = new Intent(Intent.ACTION_CALL);
                Call.setData(Uri.parse("tel:010-9173-8332"));
                startActivity(Call);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if(requestCode==REQUEST_CODE_LOCATION){
            if(grantResult.length > 0){
                for (int aGrantResult : grantResult) {
                    if (aGrantResult == PackageManager.PERMISSION_DENIED) {
                        //권한이 하나라도 거부 될 시
                        new AlertDialog.Builder(getContext())
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
                                        .setData(Uri.parse("package:" + getContext().getPackageName()));
                                getContext().startActivity(intent);
                            }
                        }).setCancelable(false).show();
                        return;
                    }
                }
            }
        }

        //지진 버튼
        btn_earthquake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지진 설명, 행동요령, 대피소
            }
        });

        //태풍 버튼
        btn_typhoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태풍 설명, 행동요령, 대피소
            }
        });

        //낙뢰 버튼
        btn_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //낙뢰 설명, 행동요령, 대피소
            }
        });

        //폭염 버튼
        btn_hitwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭염 설명, 행동요령, 대피소
            }
        });

        //호우 버튼
        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //호우 설명, 행동요령, 대피소
            }
        });

        //폭설 버튼
        btn_snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭설 설명, 행동요령, 대피소
            }
        });
    }

    private void initView() {
        current_location = getView().findViewById(R.id.current_location);
        recyclerview_current_location = getView().findViewById(R.id.recyclerview_current_location);
        btn_earthquake = getView().findViewById(R.id.btn_earthquake); //지진 버튼
        btn_typhoon = getView().findViewById(R.id.btn_typhoon); //태풍 버튼
        btn_thunder = getView().findViewById(R.id.btn_thunder); //낙뢰 버튼
        btn_hitwave = getView().findViewById(R.id.btn_hitwave); //폭염 버튼
        btn_rain = getView().findViewById(R.id.btn_rain); //호우 버튼
        btn_snow = getView().findViewById(R.id.btn_snow); //폭설 버튼
        btn_emergency = getView().findViewById(R.id.btn_emergency); //119 신고 버튼

        // GPS ON/OFF 확인하여 OFF일 시 GPS 설정화면으로 이동진행
        locationManagerGPS = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    }
}