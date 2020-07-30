package com.example.videotoimage.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.videotoimage.R;
import com.example.videotoimage.adapter.Adapter_Viewpager;
import com.google.android.material.tabs.TabLayout;

public class GalleryActivity extends AppCompatActivity {
    private ViewPager viewPager_gallery ;
    private TabLayout tabLayout_gallery ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        viewPager_gallery = findViewById(R.id.viewPager_gallery) ;
        tabLayout_gallery = findViewById(R.id.tabLayout_gallery) ;

        Adapter_Viewpager adapter_viewpager = new Adapter_Viewpager(getSupportFragmentManager(),1);
        viewPager_gallery.setAdapter(adapter_viewpager);
        tabLayout_gallery.setupWithViewPager(viewPager_gallery) ;

    }

    public void NoClick(View view) {
    }
}