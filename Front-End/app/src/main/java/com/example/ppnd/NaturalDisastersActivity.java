package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;

public class NaturalDisastersActivity extends AppCompatActivity {
    Button btn_action, btn_shelter;
    TextView text_title, text_content;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturaldisasters);

        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);


        if(type.equals("earthquake")){
            text_title.setText("");
            text_content.setText("");
        }
        else if(type.equals("typoon")){
            text_title.setText("");
            text_content.setText("");
        }
        else if(type.equals("thunder")){
            text_title.setText("");
            text_content.setText("");

        }
        else if(type.equals("heatwave")){
            text_title.setText("");
            text_content.setText("");

        }
        else if(type.equals("rain")){
            text_title.setText("");
            text_content.setText("");

        }
        else if(type.equals("snow")){
            text_title.setText("");
            text_content.setText("");

        }


        btn_action = (Button) findViewById(R.id.btn_action);
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
                startActivity(intent);
            }
        });


    }
}
