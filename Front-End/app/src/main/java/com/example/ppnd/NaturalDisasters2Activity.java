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

        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);


        if(type.equals("earthquake")){
            text_title.setText("지진");
            text_content.setText("");
        }
        else if(type.equals("heatwave")){
            text_title.setText("폭염");
            text_content.setText("");

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
                startActivity(intent);
            }
        });


    }
}
