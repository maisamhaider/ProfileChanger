package com.example.profilechanger.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.profilechanger.R;
import com.example.profilechanger.fragments.BackgroundFrag;
import com.example.profilechanger.fragments.LocationBaseProfilerFragment;
import com.example.profilechanger.fragments.TimeBaseProfilerFragment;


public class PermissionsFragmentsAdapter extends FragmentPagerAdapter {
    int[] titles = new int[]{R.string.location_permission/*, R.string.manage_settings
            , R.string.do_not_disturb*/};
    Context context;

    public PermissionsFragmentsAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return BackgroundFrag.newInstance();
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
