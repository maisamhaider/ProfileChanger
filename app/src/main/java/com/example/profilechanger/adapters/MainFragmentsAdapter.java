package com.example.profilechanger.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.profilechanger.R;
import com.example.profilechanger.fragments.LocationBaseProfilerFragment;
import com.example.profilechanger.fragments.TimeBaseProfilerFragment;


public class MainFragmentsAdapter extends FragmentPagerAdapter {
    int[] titles = new int[]{R.string.location_base,R.string.time_base};
    Context context;

    public MainFragmentsAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return LocationBaseProfilerFragment.newInstance();
            case 1:
                return TimeBaseProfilerFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(titles[position]);
    }
}
