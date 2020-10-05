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
            text_content.setText("");
        }
        else if(type.equals("thunder")){
            text_title.setText("낙뢰");
             text_content.setText("");
        }
        else if(type.equals("rain")){
            text_title.setText("호우");
            text_content.setText("");

        }
        else if(type.equals("snow")){
            text_title.setText("폭설");
            text_content.setText("");

        }


        btn_action = (Button) findViewById(R.id.btn_action1);

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
