package com.example.ppnd.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ppnd.Other.GPSService;
import com.example.ppnd.R;

import static android.content.Context.MODE_PRIVATE;

public class OptionFragment extends Fragment {
    private boolean saveData;
    private Switch notiSwitch; //알림 설정 여부 스위치
    private EditText noti1Text, noti2Text, noti3Text;
    private String noti1, noti2, noti3;
    private Button btn_save;
    private SharedPreferences spf;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_option, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        //설정 값 불러오기
        spf = getActivity().getSharedPreferences("NotiWanted", MODE_PRIVATE);
        load();

        //알림 희망 지역 설정 저장 버튼
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        //알림 여부 설정 스위치
        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //True일 때
                    Toast.makeText(getContext(), "스위치가 ON 상태입니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), GPSService.class);
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                        getContext().startForegroundService(intent);
//                    else
//                        getContext().startService(intent);
                } else {
                    //False일 때
                    Toast.makeText(getContext(), "스위치가 OFF 상태입니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), GPSService.class);
//                    getContext().stopService(intent);
                }
            }
        });
    }

    //설정 값 저장하는 함수
    private void save() {
        //SharedPreferences 객체만으로는 저장 불가능. Editor 사용
        SharedPreferences.Editor editor = spf.edit(); //sharedPrefrences를 제어한 editor 선언
        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putString("NOTI1", noti1Text.getText().toString().trim());
        editor.putString("NOTI2", noti2Text.getText().toString().trim());
        editor.putString("NOTI3", noti3Text.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
        Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    //설정 값 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveData = spf.getBoolean("NotiWanted", false);
        noti1 = spf.getString("NOTI1", "");
        noti2 = spf.getString("NOTI2", "");
        noti3 = spf.getString("NOTI3", "");

        noti1Text.setText(noti1);
        noti2Text.setText(noti2);
        noti3Text.setText(noti3);
    }

    private void initView() {
        notiSwitch = getView().findViewById(R.id.notiSwitch);
        noti1Text = getView().findViewById(R.id.noti1);
        noti2Text = getView().findViewById(R.id.noti2);
        noti3Text = getView().findViewById(R.id.noti3);
        btn_save = getView().findViewById(R.id.btn_save);
    }
}