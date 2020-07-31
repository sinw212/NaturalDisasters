package com.example.ppnd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OptionFragment extends Fragment {

    private CheckBox notiCheck;    //  노티 체크
    private CheckBox msgCheck;    //  문자 체크

    private EditText password;
    private EditText newPassword;
    private EditText newPasswordCheck;

    private Button btn_changePassword;
    private Button btn_logout;

    SharedPreferences spf;

    private void btn_init() {
        btn_changePassword = getView().findViewById(R.id.btn_changePasswrod);
        btn_logout = getView().findViewById(R.id.btn_login);
    }

    private void edit_init() {
        password = getView().findViewById(R.id.password);
        newPassword = getView().findViewById(R.id.newPassword);
        newPasswordCheck = getView().findViewById(R.id.newPasswordCheck);
    }

    private void getSPF() {
        spf = getActivity().getSharedPreferences("MemberLogin", Activity.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_option, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_init();
        getSPF();

        btn_changePassword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_init();
                String pass = password.getText().toString();
                String newPass = newPassword.getText().toString();
                String newPassCheck = newPasswordCheck.getText().toString();
                if(newPass.equals(newPassCheck)) {
                    changePassRequest(pass, newPass);
                } else {
                    Toast.makeText(getContext(), "변경할 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_logout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = spf.edit();
                editor.clear();
                editor.apply();
                getActivity().finish();
            }
        });

    }

    private void changePassRequest(final String pass, final String newPass) {
        String url = "";
        final String id = spf.getString("ID", "none");
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "ChangeSuccess":
                                Toast.makeText(getActivity(), "비밀번호 변경 성공", Toast.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("Password", newPass);
                                editor.apply();
                                break;
                            case "NoID":
                                Toast.makeText(getActivity(), "등록된 아이디가 아닙니다. ", Toast.LENGTH_SHORT).show();
                                break;
                            case "DBError":
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
                params.put("password", pass);
                params.put("newPassword", newPass);
                return params;
            }
        };
    }
}