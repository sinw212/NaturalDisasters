package com.example.ppnd.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.Adapter.VolunteerAdapter;
import com.example.ppnd.Data.VolunteerData;
import com.example.ppnd.Other.AppHelper;
import com.example.ppnd.R;
import com.example.ppnd.VolunteerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolunteerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VolunteerData> myDataset;
    private String url_volunteer = "http://tomcat.comstering.synology.me/PPND_Server/volunteer.jsp";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_volunteer,container,false);

        // request queue 는 앱이 시작되었을 때 한 번 초기화되기만 하면 계속 사용이 가능
        if(AppHelper.requestqueue == null)
            AppHelper.requestqueue = Volley.newRequestQueue(getContext());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new VolunteerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        list_AddRequest();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                VolunteerData vdata = myDataset.get(position);
                // Toast.makeText(getActivity(), vdata.writer +"," + vdata.title+",", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), VolunteerActivity.class);
                intent.putExtra("title", vdata.getTitle() );
                intent.putExtra("date", vdata.getDate() );
                intent.putExtra("writer", vdata.getWriter());
                intent.putExtra("content", vdata.getContent() );
                intent.putExtra("img", vdata.getImg());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return rootView;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private VolunteerFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final VolunteerFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    // 아이템 추가
    public void list_AddRequest() {

        // Request Obejct인 StringRequest 생성
        StringRequest request = new StringRequest(Request.Method.POST, url_volunteer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject jsonObject = jarray.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String date = jsonObject.getString("date");
                                String writer = jsonObject.getString("writer");
                                String content = jsonObject.getString("content");
                                JSONArray imgarray = jsonObject.getJSONArray("image");
                                ArrayList<String> img = new ArrayList<>();
                                for(int j = 0; j<imgarray.length(); j++){
                                    img.add(imgarray.get(j).toString());
                                }

                                myDataset.add(new VolunteerData(title,date, writer,content,img));
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("통신 메세지", "[" + response + "]"); // 서버와의 통신 결과 확인 목적

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("통신 에러", "[" + error.getMessage() + "]");
                        Log.v("통신 에러 이유",error.getStackTrace().toString());
                    }
                }) {

        };

        request.setShouldCache(false); // 이전 결과가 있더라도 새로 요청해서 응답을 보여줌
        AppHelper.requestqueue.add(request); // request queue 에 request 객체를 넣어준다.

    }

}