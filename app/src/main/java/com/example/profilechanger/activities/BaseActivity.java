package com.example.profilechanger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.utils.TimeUtil;

public class BaseActivity extends AppCompatActivity {
    MyPreferences preferences;
    TimeUtil timeUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new MyPreferences(this);
        timeUtil = new TimeUtil(this);

        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            if (preferences.getString(MyAnnotations.THEME, MyAnnotations.DAY)
//                    .matches(MyAnnotations.AUTO_CHANGE)) {
//
//                long pm7 = timeUtil.getMillisFromFormattedDate(
//                        timeUtil.getTimePlusHours(19)
//                        , MyAnnotations.DEFAULT_TIME_FORMAT);
//                long am7 = timeUtil.getMillisFromFormattedDate(
//                        timeUtil.getTimePlusHours(7)
//                        , MyAnnotations.DEFAULT_TIME_FORMAT);
//
//                long current = timeUtil.getMillisFromFormattedDate(
//                        timeUtil.getCurrentFormattedTime(),
//                        MyAnnotations.DEFAULT_TIME_FORMAT);
//
//                if (current > am7 && current < pm7) {
//                    //day
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                        setTheme(R.style.Theme_ProfileChanger);
//                    } else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    }
//                    preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
//
//                } else {//night
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                        setTheme(R.style.Theme_ProfileChanger_dark);
//                    } else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    }
//                    preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, false);
//                }
//
//            } else {
                if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {
                    setTheme(R.style.Theme_ProfileChanger);
                } else {
                    setTheme(R.style.Theme_ProfileChanger_dark);
                }

//            }
        } else {
            if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragmentsContainer, fragment).commit();
    }

    public void intent(Activity activity) {
        startActivity(new Intent(this, activity.getClass()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences == null) {
            preferences = new MyPreferences(this);
        }
        if (timeUtil == null) {
            timeUtil = new TimeUtil(this);
        }


    }
}
