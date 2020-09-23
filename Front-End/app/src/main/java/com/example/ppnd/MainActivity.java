package com.example.ppnd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ppnd.Fragment.HomeFragment;
import com.example.ppnd.Fragment.NewsFlashFragment;
import com.example.ppnd.Fragment.OptionFragment;
import com.example.ppnd.Fragment.VolunteerFragment;
import com.example.ppnd.Other.DataParsing;
import com.example.ppnd.Other.GPSService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class MainActivity extends AppCompatActivity {
    private long backKeyClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar 커스텀
//        try {
//            getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.custom_actionbar);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        private FragmentManager fragmentManager = getSupportFragmentManager();

        private HomeFragment home_fragment = new HomeFragment();
        private NewsFlashFragment newsflash_fragment = new NewsFlashFragment();
        private VolunteerFragment volunteer_fragment = new VolunteerFragment();
        private OptionFragment option_fragment = new OptionFragment();

        public String current_location_newsflash, nation_wide_newsflash;
        private Bitmap satellite_image;
        private DataParsing dataParsing = new DataParsing();

        private String current_address;
        private int current_code;

        //가장 먼저 호출
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //작업 스레드를 실행하는 함수
        //메인 스레드와는 별개로 오래 걸리는 작업 처리
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                current_address = GPSService.current_address;
                current_code = GPSService.current_code;
                Log.d("진입2222", current_address);
                Log.d("진입3333", String.valueOf(current_code));

                current_location_newsflash = dataParsing.newsflashXmlData(current_code);
                Log.d("진입333_1", current_location_newsflash);
                nation_wide_newsflash = dataParsing.newsflashXmlData(108);
                Log.d("진입333_2", nation_wide_newsflash);
                satellite_image = dataParsing.satelliteXmlData();
                Log.d("진입333_3", String.valueOf(satellite_image));

                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("MyAsyncTask InterruptedException error ");
            }

            return null;
        }

        //doInBackground() 실행이 정상적으로 처리가 완료되는 경우 실행
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.d("진입4444", "ㅇㅇ");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, home_fragment).commitAllowingStateLoss();

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    switch(item.getItemId()) {
                        case R.id.home: {
                            transaction.replace(R.id.frame_layout, home_fragment).commitAllowingStateLoss();
                            break;
                        }
                        case R.id.newsflash: {
                            transaction.replace(R.id.frame_layout, newsflash_fragment).commitAllowingStateLoss();
                            break;
                        }
                        case R.id.volunteer: {
                            transaction.replace(R.id.frame_layout, volunteer_fragment).commitAllowingStateLoss();
                            break;
                        }
                        case R.id.option: {
                            transaction.replace(R.id.frame_layout, option_fragment).commitAllowingStateLoss();
                            break;
                        }
                    }
                    return true;
                }
            });

            //번들객체 생성, 값 저장
            Bundle bundle1 = new Bundle();
            Bundle bundle2 = new Bundle();

            //fragment로 번들 전달
            home_fragment.setArguments(bundle1);
            newsflash_fragment.setArguments(bundle2);

            bundle1.putString("current_address", current_address);
            Log.d("진입444_1", current_address);
            bundle1.putString("current_location_newsflash", current_location_newsflash);
            Log.d("진입444_2", current_location_newsflash);
            bundle2.putString("nation_wide_newsflash", nation_wide_newsflash);
            Log.d("진입444_3", nation_wide_newsflash);
            bundle2.putParcelable("satellite_image", satellite_image);
            Log.d("진입444_4", String.valueOf(satellite_image));

            Log.d("진입5555", "ㅇㅇ");
        }

        //doInBackground() 실행 도중 작업이 중단되는 경우 실행
        @Override
        protected void onCancelled(Void result) {
            Log.d("진입왜???", "ㅇㅇ");
            super.onCancelled(result);
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else { // 더이상 스택에 프래그먼트가 없을 시 액티비티에서 앱 종료 여부 결정
            if (System.currentTimeMillis() > backKeyClickTime + 2000) { // 1회 누를 시 Toast
                backKeyClickTime = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyClickTime + 2000) { // 연속 2회 누를 시 activty shutdown
                ActivityCompat.finishAffinity(this);
            }
        }
    }
}