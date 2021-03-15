package com.xw.dlnaplay;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        viewPager = findViewById(R.id.viewPager);
        for (int i = 0; i < 3; i++) {
            TestFragment testFragment = TestFragment.newInstance("标题" + i);
            fragmentList.add(testFragment);
        }

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this,fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
    }
}