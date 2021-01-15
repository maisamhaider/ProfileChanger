package com.example.profilechanger.utils;

import android.app.Application;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.google.android.gms.ads.MobileAds;

public class ApplicationLevel extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        MyPreferences preferences = new MyPreferences(this);

        if (preferences.getBoolean(MyAnnotations.IS_APP_FIRST_TIME, true)) {
            preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
            preferences.setBoolean(MyAnnotations.IS_APP_FIRST_TIME, false);

        }
        MobileAds.initialize(this, initializationStatus -> {

        });
    }
}
