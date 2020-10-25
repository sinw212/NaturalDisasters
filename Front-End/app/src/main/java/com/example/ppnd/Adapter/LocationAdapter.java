package com.example.ppnd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppnd.Data.LocationData;
import com.example.ppnd.NaturalDisasters1Activity;
import com.example.ppnd.NaturalDisasters2Activity;
import com.example.ppnd.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LocationData> locationData;

    private Intent intent;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView dataText;

        public ViewHolder(View itemView) {
            super(itemView); // 입력 받은 값을 뷰홀더에 삽입
            dataText = itemView.findViewById(R.id.location_newsflash);
        }
    }

    public LocationAdapter (Context mContext, ArrayList<LocationData> list) {
        this.mContext = mContext;
        this.locationData = list; // 처리하고자하는 아이템 리스트
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_current_location,viewGroup,false); // 뷰생성
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    //실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder holder, int position) {
        final LocationData item = locationData.get(position); // 위치에 따른 아이템 반환
        holder.dataText.setText(item.getData()); // LocationData getData값을 datatext에 삽입

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getData().contains("지진")) { //지진
                    intent = new Intent(v.getContext(), NaturalDisasters2Activity.class);
                    intent.putExtra("type", "newsflash_earthquake");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().contains("풍랑") || item.getData().contains("강풍")) { //태풍
                    intent = new Intent(v.getContext(), NaturalDisasters1Activity.class);
                    intent.putExtra("type", "newsflash_typhoon");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().contains("낙뢰") || item.getData().contains("번개")) { //낙뢰
                    intent = new Intent(v.getContext(), NaturalDisasters1Activity.class);
                    intent.putExtra("type", "newsflash_thunder");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().contains("폭염")) { //폭염
                    intent = new Intent(v.getContext(), NaturalDisasters2Activity.class);
                    intent.putExtra("type", "newsflash_heatwave");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().contains("호우") || item.getData().contains("폭우")) { //호우
                    intent = new Intent(v.getContext(), NaturalDisasters1Activity.class);
                    intent.putExtra("type", "newsflash_rain");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().contains("폭설")) { //폭설
                    intent = new Intent(v.getContext(), NaturalDisasters1Activity.class);
                    intent.putExtra("type", "newsflash_snow");
                    intent.putExtra("data", item.getData());
                    v.getContext().startActivity(intent);
                }
                else if(item.getData().equals("현재 속보가 존재하지 않습니다."))
                    Toast.makeText(v.getContext(), "현재 속보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                else if(item.getData().equals("오류가 발생했습니다. 다시 시도해주세요."))
                    Toast.makeText(v.getContext(), "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //LocationAdapter 관리하는 아이템의 개수를 반환
    @Override
    public int getItemCount() {
        return locationData.size();
    }
}