package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Adapter.VolunteerImgAdapter;

import java.util.ArrayList;

public class VolunteerActivity extends AppCompatActivity {

    private TextView title, date, writer,content;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> imgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        String titled = intent.getExtras().getString("title");
        String dated = intent.getExtras().getString("date");
        String writerd = intent.getExtras().getString("writer");
        String contentd = intent.getExtras().getString("content");
        imgd = intent.getStringArrayListExtra("img");

        title = (TextView) findViewById(R.id.volunteercon_title);
        date = (TextView) findViewById(R.id.volunteercon_date);
        writer = (TextView) findViewById(R.id.volunteercon_writer);
        content = (TextView) findViewById(R.id.volunteercon_content);

        title.setText(titled);
        date.setText(dated);
        writer.setText(writerd);
        content.setText(contentd);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview3);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new VolunteerImgAdapter(imgd, this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        /*
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.requestFocus(View.FOCUS_UP);
        scrollView.scrollTo(0,0);
        */
    }
}