package com.example.videotoimage.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.videotoimage.fragments.PictureGallery_Fragment;
import com.example.videotoimage.fragments.VideoGallery_Fragment;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Viewpager extends FragmentPagerAdapter {
    private List<Fragment> fragmentList ;

    public Adapter_Viewpager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList = new ArrayList<>();
        fragmentList.add(new VideoGallery_Fragment());
        fragmentList.add(new PictureGallery_Fragment()) ;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "Videos" ;
            case 1 :
                return "Images" ;
            default:
                return "" ;
        }
    }
}
