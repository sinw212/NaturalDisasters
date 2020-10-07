package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VolunteerActivity extends AppCompatActivity {

    private TextView title, date, writer,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String titled = intent.getExtras().getString("title");
        String dated = intent.getExtras().getString("date");
        String writerd = intent.getExtras().getString("writer");

        title = (TextView) findViewById(R.id.volunteercon_title);
        date = (TextView) findViewById(R.id.volunteercon_date);
        writer = (TextView) findViewById(R.id.volunteercon_writer);
        content = (TextView) findViewById(R.id.volunteercon_content);

        title.setText(titled);
        date.setText(dated);
        writer.setText(writerd);
        content.setText("태풍으로 인하여 많은 피해를 입은 충청남도 천안시에서 피해복구 활동을 할 자원봉사자를 모집합니다.\n많은 참여 부탁드립니다.\n\n"+
                "일  시 : 2020. 10. 10(목) 07:00 ~ 20:00\n인  원 : 성인 40명 (선착순 모집)\n" +
                "내  용 : 침수가옥 복구 및 토사물 청소\n" +
                "신  청 : 전화 (T.010.1234.5678)\n" +
                "준비물 : 작업에 적절한 옷차림 (긴팔, 긴바지, 장화, 등산화, 작업화)/ 모자, 수건, 여벌 옷 등\n" +
                "센터지원 : 식사/ 물/ 장비 등");



    }
}
