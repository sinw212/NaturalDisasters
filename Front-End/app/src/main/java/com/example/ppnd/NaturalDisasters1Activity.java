package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NaturalDisasters1Activity extends AppCompatActivity {
    Button btn_action, btn_shelter;
    TextView text_title, text_content;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturaldisasters1);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);


        if(type.equals("typoon")){
            text_title.setText("태풍");
            text_content.setText(" 태양으로부터 오는 열에너지는 지구의 날씨를 변화시키는 주된 원인으로 작용한다. 지구는 구형으로 되어 있어 저위도와 고위도 사이에는 열에너지 불균형이 나타난다. 태양의 고도각이 높아 많은 에너지를 축적한 적도부근의 바다에서는 대류구름들이 만들어지게 된다. 때때로 이러한 대류구름들이 모여 거대한 저기압 시스템으로 발달하게 되는데, 이를 태풍이라고 부른다. 태풍은 바다로부터 증발한 수증기를 공급받아 강도를 유지하면서 고위도로 이동하게 된다. 이러한 과정을 통해 태풍은 지구 남북 간의 에너지 불균형을 해소하는 역할을 한다.\n[기상청] 태풍 ");
        }
        else if(type.equals("thunder")){
            text_title.setText("낙뢰");
            text_content.setText(" 낙뢰는 뇌전 현상의 일부로 구름으로부터 지면으로 방전현상이 일어나는 것을 의미한다. 번개(lightning)는 발달한 구름대에서 발생하는 전기적 현상으로 구름 내부나 구름과 구름사이로 혹은 구름에서 주변 대기나 지면으로 이동하는 섬광을 말한다.\n" +
                    "[네이버 지식백과] 낙뢰 [Lightning Stroke] (기상학백과)\n" +
                    "\n");
        }
        else if(type.equals("rain")){
            text_title.setText("호우");
            text_content.setText(" 국지적으로 단시간내에 많은 양의 강한 비가 내리는 현상을 말한다. 홍수·사태 등이 재해를 수반하게 된다. 원래 이 용어는 보도 관계자들에 의해서 만들어진 것이지만 현재는 거의 기상용어로 사용되고 있다.\n" +
                    "집중호우에 대한 특별한 강수량의 기준은 없으며 다만 한 시간에 20mm이상 혹은 하루에 100mm이상의 비가 내릴 때를 집중호우라고 여긴다.(참고로 우리나라 연평균 강수량은 1200㎜ 가량임)\n" +
                    "[네이버 지식백과] 집중호우 (시사상식사전, pmg 지식엔진연구소)\n" +
                    "\n");

        }
        else if(type.equals("snow")){
            text_title.setText("폭설");
            text_content.setText(" 폭설(대설)이란 많은 눈이 시간적, 공간적으로 집중되어 내리는 현상을 말한다. 기상청의 대설 특보기준을 보면 주의보는 24시간 신적설(새로 쌓인 눈)이 5cm이상 예상될 때 발령된다. 대설경보는 24시간 신적설이 20cm이상 예상될 때다. 다만, 산지는 24시간 신적설이 30cm이상 예상될 때 발령된다.\n" +
                    "[네이버 지식백과] 폭설 [Heavy Snow] - 눈, 많이 오면 폭탄 같은 재앙 (지구과학산책, 반기성)\n" +
                    "\n");

        }


        btn_action = (Button) findViewById(R.id.btn_action1);
        btn_shelter = (Button) findViewById(R.id.btn_shelter);

        btn_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActionActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });


    }
}
