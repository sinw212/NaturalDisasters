package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class ActionActivity extends AppCompatActivity {

    String type;

    TabLayout tabs;

    ActionFragment1 fragment1;
    ActionFragment2 fragment2;
    ActionFragment3 fragment3;
    ActionFragment4 fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        fragment1 = new ActionFragment1();
        fragment2 = new ActionFragment2();
        fragment3 = new ActionFragment3();
        fragment4 = new ActionFragment4();

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        //번들객체 생성, text값 저장
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        //fragment1로 번들 전달
        fragment1.setArguments(bundle);

        tabs = findViewById(R.id.tabs);

        if (type.equals("earthquake")) {
            tabs.addTab(tabs.newTab().setText("평소 대비"));
            tabs.addTab(tabs.newTab().setText("지진 발생시"));
            tabs.addTab(tabs.newTab().setText("지진 발생 후"));
        } else if (type.equals("typhoon")) {
            tabs.addTab(tabs.newTab().setText("태풍 예보 시"));
            tabs.addTab(tabs.newTab().setText("태풍 특보 중"));
            tabs.addTab(tabs.newTab().setText("태풍 이후"));
        } else if (type.equals("heatwave")) {
            tabs.addTab(tabs.newTab().setText("평상시"));
            tabs.addTab(tabs.newTab().setText("폭염발생 시"));
            tabs.addTab(tabs.newTab().setText("폭염 관련 정보"));
        } else if (type.equals("thunder")) {
            tabs.addTab(tabs.newTab().setText("낙뢰 예상 시"));
            tabs.addTab(tabs.newTab().setText("낙뢰가 발생할 때"));
            tabs.addTab(tabs.newTab().setText("응급처치 행동요령"));
            tabs.addTab(tabs.newTab().setText("낙뢰 관련 정보"));
        } else if (type.equals("rain")) {
            tabs.addTab(tabs.newTab().setText("사전준비"));
            tabs.addTab(tabs.newTab().setText("호우특보 예보시"));
            tabs.addTab(tabs.newTab().setText("호우특보 중"));
            tabs.addTab(tabs.newTab().setText("호우 이후"));
        } else if (type.equals("snow")) {
            tabs.addTab(tabs.newTab().setText("평상시 대설대비"));
            tabs.addTab(tabs.newTab().setText("대설 예보 시"));
            tabs.addTab(tabs.newTab().setText("대설 특보 중"));
            tabs.addTab(tabs.newTab().setText("대설 후"));
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if (position == 0)
                    selected = fragment1;
                else if (position == 1)
                    selected = fragment2;
                else if (position == 2)
                    selected = fragment3;
                else if (position == 3)
                    selected = fragment4;

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                //번들객체 생성, text값 저장
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                //fragment1로 번들 전달
                selected.setArguments(bundle);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
