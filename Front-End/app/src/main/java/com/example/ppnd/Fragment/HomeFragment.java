package com.example.ppnd.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ppnd.Adapter.CurrentLocationAdapter;
import com.example.ppnd.Data.CurrentLocationData;
import com.example.ppnd.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    // GPS
    private LocationManager locationManagerGPS;

    private RequestQueue requestQueue;
    private String serviceKey;
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private String date;

    private RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private CurrentLocationAdapter currentlocationAdapter;
    private ArrayList<CurrentLocationData> arrayList;
    private CurrentLocationData currentlocationData;

    private TextView current_location;
    private Button btn_earthquake, btn_typhoon, btn_thunder,
            btn_hitwave, btn_rain, btn_snow, btn_emergency;

    private String data;
    private StringBuffer buffer = new StringBuffer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        currentlocationAdapter = new CurrentLocationAdapter(arrayList);
        recyclerview.setAdapter(currentlocationAdapter);

        //GPS 미 설정시 false, 설정 시 true
        if (!locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS 설정 off일 시 설정화면으로 이동
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle("GPS 미설정 알림")
                    .setIcon(R.drawable.main_icon)
                    .setMessage("GPS가 미설정 되어있어, 위급 시 속보 알림을 받지 못할 우려가 있으니 GPS 사용을 허용해주세요\n" +
                            "허용한 후, 원활한 사용을 위해 앱을 다시 켜주십시오.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent setting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            setting.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(setting);
                            getActivity().finish();
                        }
                    }).show();
        }

        serviceKey = "ic1bRMghX2rxMK8sUa%2B2cyNOyPqz96fTfOIbi1fHykBtmAg4D2B46M2fsdC8z7B%2ByeS0xeIsXdmiKqIrUFdevA%3D%3D";

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }

        //XML 파싱해서 Adapter에 연결하는 과정
        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = current_location_XmlData(); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String split_data[] = data.split("\n");
                        for(int i=0; i<split_data.length; i++) {
                            Log.d("진입",split_data[i]);

                            currentlocationData = new CurrentLocationData(split_data[i]);

                            arrayList.add(currentlocationData); // RecyclerView의 마지막 줄에 삽입
                            currentlocationAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();

        //지진 버튼
        btn_earthquake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지진 설명, 행동요령, 대피소
            }
        });

        //태풍 버튼
        btn_typhoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태풍 설명, 행동요령, 대피소
            }
        });

        //낙뢰 버튼
        btn_thunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //낙뢰 설명, 행동요령, 대피소
            }
        });

        //폭염 버튼
        btn_hitwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭염 설명, 행동요령, 대피소
            }
        });

        //호우 버튼
        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //호우 설명, 행동요령, 대피소
            }
        });

        //폭설 버튼
        btn_snow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //폭설 설명, 행동요령, 대피소
            }
        });

        //119 신고버튼 (GPS 기능 필요없음)
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Call = new Intent(Intent.ACTION_CALL);
                Call.setData(Uri.parse("tel:010-9173-8332"));
                startActivity(Call);
            }
        });
    }

    //현재 위치 기상청 특보
    private String current_location_XmlData() {
        try {
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnMsg?serviceKey=" +
                    serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&stnId=108&fromTmFc="+date+"&toTmFc="+date+"&");
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while(parserEvent != XmlPullParser.END_DOCUMENT) {
                switch(parserEvent) {
                    case XmlPullParser.START_TAG: //parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("t2")) { //속보 발생 해당구역
                            parser.next();
                            buffer.append(parser.getText());//t2 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;
                    case XmlPullParser.TEXT: //parser가 내용에 접근했을 때
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")) {
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d("진입11", buffer.toString());
        Log.d("진입22", buffer.toString().replace("  ", ""));
        return buffer.toString().replace("  ", "");
    }

    private void initView() {
        current_location = getView().findViewById(R.id.current_location);
        recyclerview = getView().findViewById(R.id.recyclerview_current_location);
        btn_earthquake = getView().findViewById(R.id.btn_earthquake); //지진 버튼
        btn_typhoon = getView().findViewById(R.id.btn_typhoon); //태풍 버튼
        btn_thunder = getView().findViewById(R.id.btn_thunder); //낙뢰 버튼
        btn_hitwave = getView().findViewById(R.id.btn_hitwave); //폭염 버튼
        btn_rain = getView().findViewById(R.id.btn_rain); //호우 버튼
        btn_snow = getView().findViewById(R.id.btn_snow); //폭설 버튼
        btn_emergency = getView().findViewById(R.id.btn_emergency); //119 신고 버튼

        //오늘 날짜 받아오기
        date = format.format(new Date());

        //GPS ON/OFF 확인하여 OFF일 시 GPS 설정화면으로 이동진행
        locationManagerGPS = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    }
}