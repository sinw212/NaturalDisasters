package com.example.ppnd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ppnd.Fragment.HomeFragment;
import com.example.ppnd.Fragment.NewsFlashFragment;
import com.example.ppnd.Fragment.OptionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import static androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class NavigationBarMainActivity extends AppCompatActivity {
    private long backKeyClickTime = 0;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private HomeFragment home_fragment = new HomeFragment();
    private NewsFlashFragment newsflash_fragment = new NewsFlashFragment();
    private OptionFragment option_fragment = new OptionFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayOptions(DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        } catch(Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_navigationbar_main);

        //MainActivity의 인텐트를 받아서 text값을 저장
        Intent intent = getIntent();
        String current_address = intent.getStringExtra("current_address");
        String current_location_newsflash = intent.getStringExtra("current_location_newsflash");
        String nation_wide_newsflash = intent.getStringExtra("nation_wide_newsflash");
        Bitmap satellite_image = intent.getParcelableExtra("satellite_image");

        Log.d("진입33", current_address);

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

        bundle1.putString("current_address",current_address);
        bundle1.putString("current_location_newsflash",current_location_newsflash);
        bundle2.putString("nation_wide_newsflash",nation_wide_newsflash);
        bundle2.putParcelable("satellite_image", satellite_image);

        Log.d("진입333", current_address);
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