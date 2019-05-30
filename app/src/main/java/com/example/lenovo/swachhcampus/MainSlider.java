package com.example.lenovo.swachhcampus;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainSlider extends AppCompatActivity {
    private ViewPager viewPager;
    private MSlideAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slider);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myadapter = new MSlideAdapter(this);
        viewPager.setAdapter(myadapter);
    }
}
