package com.example.ppnd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NaturalDisasters2Activity extends AppCompatActivity {
    private LinearLayout linearlayout_background;
    private Button btn_behavior, btn_shelter;
    private TextView text_title, text_content;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturaldisasters2);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        linearlayout_background = findViewById(R.id.linearlayout_background);
        btn_behavior = findViewById(R.id.btn_action2);
        btn_shelter = findViewById(R.id.btn_shelter);
        text_title = findViewById(R.id.text_title);
        text_content = findViewById(R.id.text_content);

        if(type.equals("heatwave")){
            linearlayout_background.setBackground(ContextCompat.getDrawable(this, R.drawable.heatwave_background));
            text_title.setText("폭염");
            text_content.setText(" 폭염이란 단순한 더위가 아닌 비정상적인 고온 현상이 여러 날 지속되는 것이다.\n\n" +
                    "우리나라의 경우 한낮의 일최고기온이 섭씨 33도 이상인 날이 2일 이상 지속될 것으로 예상될 때, '폭염주의보'를, 섭씨 35도 이상으로 2일 이상 지속될 것으로 예상될 때, '폭염경보'를 발령한다.\n");
        }

        btn_behavior.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //행동요령
            }
        });

        btn_shelter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //대피소
            }
        });
    }
}