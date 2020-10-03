package com.example.ppnd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NaturalDisasters1Activity extends AppCompatActivity {
    private LinearLayout linearlayout_background;
    private Button btn_behavior;
    private TextView text_title, text_content;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturaldisasters1);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        linearlayout_background = findViewById(R.id.linearlayout_background);
        btn_behavior = findViewById(R.id.btn_action);
        text_title = findViewById(R.id.text_title);
        text_content = findViewById(R.id.text_content);

        if(type.equals("typhoon")) {
            linearlayout_background.setBackground(ContextCompat.getDrawable(this,R.drawable.typhoon_background));
            text_title.setText("태풍");
            text_content.setText(" 태양의 고도각이 높아 많은 에너지를 축적한 적도부근의 바다에서는 대류구름들이 만들어지게 된다. 때때로 이러한 대류구름들이 모여 거대한 저기압 시스템으로 발달하게 되는데, 이를 태풍이라고 부른다.\n\n" +
                    " 태풍은 바다로부터 증발한 수증기를 공급받아 강도를 유지하면서 고위도로 이동하게 된다. 이러한 과정을 통해 태풍은 지구 남북 간의 에너지 불균형을 해소하는 역할을 한다.\n");
        }
        else if(type.equals("thunder")) {
            linearlayout_background.setBackground(ContextCompat.getDrawable(this,R.drawable.thunder_background));
            text_title.setText("낙뢰");
            text_content.setText(" 낙뢰는 뇌전 현상의 일부로 구름으로부터 지면으로 방전현상이 일어나는 것을 의미한다.\n" +
                    " 번개(lightning)는 발달한 구름대에서 발생하는 전기적 현상으로 구름 내부나 구름과 구름사이로 혹은 구름에서 주변 대기나 지면으로 이동하는 섬광을 말한다.\n");
        }
        else if(type.equals("rain")) {
            linearlayout_background.setBackground(ContextCompat.getDrawable(this,R.drawable.rain_background));
            text_title.setText("호우");
            text_content.setText(" 국지적으로 단시간내에 많은 양의 강한 비가 내리는 현상을 말한다. 홍수·사태 등이 재해를 수반하게 된다.\n\n" +
                    " 집중호우에 대한 특별한 강수량의 기준은 없으며 다만 한 시간에 20mm이상 혹은 하루에 100mm이상의 비가 내릴 때를 집중호우라고 여긴다.(참고로 우리나라 연평균 강수량은 1200㎜ 가량임)\n");
        }
        else if(type.equals("snow")) {
            linearlayout_background.setBackground(ContextCompat.getDrawable(this,R.drawable.snow_background));
            text_title.setText("폭설");
            text_content.setText(" 폭설(대설)이란 많은 눈이 시간적, 공간적으로 집중되어 내리는 현상을 말한다.\n\n" +
                    " 기상청의 대설 특보기준을 보면 주의보는 24시간 신적설(새로 쌓인 눈)이 5cm이상 예상될 때 발령된다. 대설경보는 24시간 신적설이 20cm이상 예상될 때다. 다만, 산지는 24시간 신적설이 30cm이상 예상될 때 발령된다.\n");
        }

        btn_behavior.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //행동요령
            }
        });
    }
}