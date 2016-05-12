package com.example.faiz.mylogin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class Tab_Adapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> arrayList;


    public Tab_Adapter(FragmentManager fm,ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList=arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
