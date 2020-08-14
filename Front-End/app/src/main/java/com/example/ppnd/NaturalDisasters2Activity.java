package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppnd.Data.EarthquakeShelterData;
import com.example.ppnd.Data.HeatWaveShelterData;

import java.util.ArrayList;

public class NaturalDisasters2Activity extends AppCompatActivity {
    Button btn_action, btn_shelter;
    TextView text_title, text_content;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturaldisasters2);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");
        final ArrayList<EarthquakeShelterData> list1 = (ArrayList<EarthquakeShelterData>) intent.getSerializableExtra("earthquakeshelter");
        final ArrayList<HeatWaveShelterData> list2 = (ArrayList<HeatWaveShelterData>) intent.getSerializableExtra("heatwaveshelter");

        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);


        if(type.equals("earthquake")){
            text_title.setText("지진");
            text_content.setText(" 지진은 지구 내부에서 오랜기간 축적된 에너지가 갑작스럽게 방출되어 지구 또는 지표를 흔드는 현상이다. 지진은 다양한 원인에 의해 발생할 수 있다. 지표면의 단층대가 끊어지면서 발생할 수 있으며, 지하 내부에서의 폭발, 지하 마그마의 이동, 탄광 폭발, 산사태, 조석력 그리고 지하수의 순환 과정에서 크고 작은 지진들이 발생한다.\n" +
                    "[네이버 지식백과] 지진 [Earthquake] (지질학백과)\n" +
                    "\n");
        }
        else if(type.equals("heatwave")){
            text_title.setText("폭염");
            text_content.setText(" 폭염은 비정상적인 고온 현상이 여러 날 지속되는 것으로 습도도 높은 경우 불쾌감을 주며, 장기간 이어질 경우 일사병, 열사병 및 호흡기 질환 등 온열 질환을 유도하고 사망까지 이르게 되는 자연재해 가운데 하나이다. 기상재해 중에서 사망자를 가장 많이 발생시킬 수 있으므로 심각한 재해로 인식된다.\n" +
                    "폭염은 지역마다 다르게 정의되는데, 이는 기후대에 따라 사람의 적응도가 다르기 때문이다. 폭염의 정의는 절대적 기준과 상대적 기준이 있어 이를 선택적으로 사용하고 있다. 우리나라 기상청의 경우 한낮의 일최고기온이 섭씨 33도 이상인 날이 2일 이상 지속될 것으로 예상될 때, ‘폭염주의보’를, 섭씨 35도 이상으로 2일 이상 지속될 것으로 예상될 때, '폭염경보'를 발령한다. 중국의 경우에는 우리나라보다 높은 섭씨 35도를 기준으로 하고 있으며, 미국은 기온 뿐만 아니라 습도도 고려하여 열 지수를 선정하고 있다.\n" +
                    "[네이버 지식백과] 폭염 [Heatwave] (기상학백과)\n" +
                    "\n");

        }


        btn_action = (Button) findViewById(R.id.btn_action2);
        btn_shelter = (Button) findViewById(R.id.btn_shelter);

        btn_action.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActionActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        btn_shelter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShelterActivity.class);
                intent.putExtra("type",type);
                if(type.equals("earthquake")){
                    intent.putExtra("earthquakeshelter", list1);
                }
                else{
                    intent.putExtra("heatwaveshelter", list2);
                }
                startActivity(intent);
            }
        });


    }
}
