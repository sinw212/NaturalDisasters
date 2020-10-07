package com.example.ppnd.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


import com.example.ppnd.Adapter.VolunteerAdapter;
import com.example.ppnd.Data.VolunteerData;
import com.example.ppnd.R;
import com.example.ppnd.VolunteerActivity;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VolunteerData> myDataset;
    String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_volunteer,container,false);

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

        myDataset.add( new VolunteerData("자원봉사 모집", "2020-09-24","천안시청"));
        myDataset.add( new VolunteerData("태풍 '하이삭' ..", "2020-09-20","공주대"));
        myDataset.add( new VolunteerData("태풍 피해복구 자원봉사자 모집", "2020-09-20","삼척시"));
        myDataset.add( new VolunteerData("재난복구현장 자원봉사", "2020-09-19","자원봉사센터"));
        myDataset.add( new VolunteerData("지진 피해 자원봉사 모집", "2020-09-15","광주광역시 자원봉사센터"));
        myDataset.add( new VolunteerData("강원도(강릉) 피해복구 지원..", "2020-09-02","인천시 자원봉사센터"));
        myDataset.add( new VolunteerData("시흥시 자원봉사 모집", "2020-09-01","시흥시 자원봉사센터"));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                VolunteerData vdata = myDataset.get(position);
                // Toast.makeText(getActivity(), vdata.writer +"," + vdata.title+",", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), VolunteerActivity.class);
                intent.putExtra("title", vdata.getTitle() );
                intent.putExtra("date", vdata.getDate() );
                intent.putExtra("writer", vdata.getWriter() );
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

}