package com.example.newspaper.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newspaper.Fragment.BusinessFragment;
import com.example.newspaper.Fragment.CurrentEventFragment;
import com.example.newspaper.Fragment.HealthFragment;
import com.example.newspaper.Fragment.ScienceFragment;
import com.example.newspaper.Fragment.TravelFragment;
import com.example.newspaper.Fragment.WorldFragment;

import java.util.ArrayList;
import java.util.List;


public class HomeAdapter extends FragmentPagerAdapter {

        WorldFragment worldFragment = new WorldFragment();
        CurrentEventFragment currentEventFragment = new CurrentEventFragment();
        BusinessFragment businessFragment = new BusinessFragment();
        HealthFragment healthFragment = new HealthFragment();
        TravelFragment travelFragment = new TravelFragment();
        ScienceFragment scienceFragment = new ScienceFragment();

        List<Fragment> fragmentList = new ArrayList<>();

        public HomeAdapter(@NonNull FragmentManager fm) {
            super(fm);

            fragmentList.add(worldFragment);
            fragmentList.add(currentEventFragment);
            fragmentList.add(businessFragment);
            fragmentList.add(healthFragment);
            fragmentList.add(travelFragment);
            fragmentList.add(scienceFragment);
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
            if (position ==0 ){
                return "Thế giới";
            }
            if (position == 1){
                return "Thời sự";
            }

            if (position == 2){
                return "Kinh doanh";
            }
            if (position == 3){
                return "Sức khỏe";
            }
            if (position == 4){
                return "Du lịch";
            }
            if (position == 5){
                return "Khoa học";
            }
            return super.getPageTitle(position);
        }
    }

