package com.example.ppnd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText idEdit;
    private EditText pwEdit;
    private EditText nameEdit;
    private EditText phoneEdit;

    private Button sign;

    private String id;
    private String pw;
    private String name;
    private String phone;

    private static RequestQueue requestQueue;

    private void init() {
        idEdit = findViewById(R.id.sign_id);
        pwEdit = findViewById(R.id.sign_password);
        nameEdit = findViewById(R.id.sign_name);
        phoneEdit = findViewById(R.id.sign_phone);
        sign = findViewById(R.id.btn_sign);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        sign.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Check", "Click Button");
                id = idEdit.getText().toString();
                pw = pwEdit.getText().toString();
                name = nameEdit.getText().toString();
                phone = phoneEdit.getText().toString();
                if(requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                }
                signUpRequest();
            }
        });
    }

    private void signUpRequest() {
        Log.d("Check", "in Request");
        String url = "http://tomcat.comstering.synology.me/PPND_Server/Join.jsp";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Check", response);
                        switch (response) {
                            case "JoinSuccess":
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case "AlreadyID":
                                Toast.makeText(getApplicationContext(), "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e("volley", response);
                                break;
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                params.put("name", name);
                params.put("phone", phone);
                params.put("password", pw);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.d("Check", "call request");
    }
}