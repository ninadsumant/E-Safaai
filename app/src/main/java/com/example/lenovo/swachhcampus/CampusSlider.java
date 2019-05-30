package com.example.lenovo.swachhcampus;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CampusSlider extends AppCompatActivity {
    private ViewPager viewPager;
    private CSliderAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_slider);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myadapter = new CSliderAdapter(this);
        viewPager.setAdapter(myadapter);
    }
}
