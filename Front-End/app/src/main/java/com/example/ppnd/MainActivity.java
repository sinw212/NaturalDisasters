package com.example.ppnd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    private EditText idEdit;    //  아이디
    private EditText pwEdit;    //  비밀번호

    private TextView signUp;    //  회원가입

    private Button btn_login;    //  로그인 버튼

    private CheckBox auto_login;    //  자동로그인

    private SharedPreferences auto;

    private static RequestQueue requestQueue;

    private void init() {
        idEdit = findViewById(R.id.login_id);
        pwEdit = findViewById(R.id.login_pw);
        signUp = findViewById(R.id.signup);
        btn_login = findViewById(R.id.btn_login);
        auto_login = findViewById(R.id.cb_autologin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auto = getSharedPreferences("MemberLogin", Activity.MODE_PRIVATE);
        if(auto.getBoolean("Auto", false)) {
            LoginPass();
        }

        init();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                }

                loginRequest(idEdit.getText().toString(), pwEdit.getText().toString());
            }
        });
    }

    private void LoginPass() {
        Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), NavigationBarMainActivity.class);
        startActivity(intent);
    }

    private void loginRequest(final String id, final String password) {
        String url = "http://tomcat.comstering.synology.me/PPND_Server/Login.jsp";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Check", response);
                        switch (response) {
                            case "LoginSuccess":
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                if(auto_login.isChecked()) {
                                    auto = getSharedPreferences("MemberLogin", Activity.MODE_PRIVATE);    //  해당앱 말고는 접근 불가
                                    SharedPreferences.Editor editor = auto.edit();
                                    editor.putString("ID", id);
                                    editor.putString("Password", password);
                                    editor.putBoolean("Auto", true);
                                    editor.apply();
                                }
                                LoginPass();
                                break;
                            case "LoginFail":
                                Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "NoID":
                                Toast.makeText(getApplicationContext(), "등록된 아이디가 아닙니다. ", Toast.LENGTH_SHORT).show();

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
                params.put("password", password);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.d("Check", "call request");
    }
}
