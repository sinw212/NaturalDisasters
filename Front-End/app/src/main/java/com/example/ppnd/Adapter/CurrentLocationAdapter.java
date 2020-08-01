package com.example.ppnd.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Data.CurrentLocationData;
import com.example.ppnd.R;

import java.util.ArrayList;

public class CurrentLocationAdapter extends RecyclerView.Adapter<CurrentLocationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CurrentLocationData> currentlocationData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView dataText;

        public ViewHolder(View itemView) {
            super(itemView); // 입력 받은 값을 뷰홀더에 삽입
            dataText = itemView.findViewById(R.id.current_location_newsflash);
        }
    }

    public CurrentLocationAdapter (ArrayList<CurrentLocationData> list) {
        this.currentlocationData = list; // 처리하고자하는 아이템 리스트
    }

    @Override
    public CurrentLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_current_location,viewGroup,false); // 뷰생성
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(CurrentLocationAdapter.ViewHolder holder, int position) {
        final CurrentLocationData item = currentlocationData.get(position); // 위치에 따른 아이템 반환
        holder.dataText.setText(item.getData()); // CurrentLocationData getData값을 datatext에 삽입

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아마도? 새로운 엑티비티 띄울
            }
        });
    }

    // CurrentLocationAdapter 관리하는 아이템의 개수를 반환
    @Override
    public int getItemCount() {
        return currentlocationData.size();
    }
}