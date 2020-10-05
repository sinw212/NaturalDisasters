package com.example.ppnd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ppnd.Data.EarthquakeShelterData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NavigationBarMainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private HomeFragment home_fragment = new HomeFragment();
    private NewsFlashFragment newsflash_fragment = new NewsFlashFragment();
    private VolunteerFragment volunteer_fragment = new VolunteerFragment();
    private OptionFragment option_fragment = new OptionFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationbar_main);

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
    }
}