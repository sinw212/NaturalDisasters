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


        btn_behavior.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //행동요령
            }
        });
    }
}