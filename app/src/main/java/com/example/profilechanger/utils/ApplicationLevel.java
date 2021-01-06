package com.example.profilechanger.utils;

import android.app.Application;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.profilechanger.R;
import com.example.profilechanger.activities.SettingsActivity;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class ApplicationLevel extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        MyPreferences preferences = new MyPreferences(this);

//        TimeUtil timeUtil = new TimeUtil(this);
//        long pm7 = timeUtil.getMillisFromFormattedDate(
//                timeUtil.getTimePlusHours(19)
//                , MyAnnotations.DEFAULT_TIME_FORMAT);
//        long am7 = timeUtil.getMillisFromFormattedDate(
//                timeUtil.getTimePlusHours(7)
//                , MyAnnotations.DEFAULT_TIME_FORMAT);
//
//        long current = timeUtil.getMillisFromFormattedDate(
//                timeUtil.getCurrentFormattedTime(),
//                MyAnnotations.DEFAULT_TIME_FORMAT);

        //night

        if (preferences.getBoolean(MyAnnotations.IS_APP_FIRST_TIME, true)) {
            preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
            preferences.setBoolean(MyAnnotations.IS_APP_FIRST_TIME, false);

        }
//        else {
//            preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, current > am7 && current < pm7);
//        }


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }
}
