package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String type = intent.getExtras().getString("type");

        if(type.equals("earthquake")){

        }
        else if(type.equals("typoon")){

        }
        else if(type.equals("thunder")){


        }
        else if(type.equals("heatwave")){


        }
        else if(type.equals("rain")){


        }
        else if(type.equals("snow")){


        }


    }
}
