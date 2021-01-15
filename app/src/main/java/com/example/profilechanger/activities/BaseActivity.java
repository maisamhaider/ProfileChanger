package com.example.profilechanger.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class BaseActivity extends AppCompatActivity {
    MyPreferences preferences;
    TimeUtil timeUtil;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new MyPreferences(this);
        timeUtil = new TimeUtil(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {
                setTheme(R.style.Theme_ProfileChanger);
            } else {
                setTheme(R.style.Theme_ProfileChanger_dark);
            }

//            }
        } else {

           int theme =  AppCompatDelegate.getDefaultNightMode();
            switch (theme)
            {
                case AppCompatDelegate.MODE_NIGHT_NO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
                    break;
                case AppCompatDelegate.MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, false);
                    break;
            }
        }
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial));
        loadInterstitial();


    }



    public void intent(Activity activity) {
        startActivity(new Intent(this,
                activity.getClass()));
    }

    public void Load_withAds(Context context, Activity activity) {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {

                    startActivity(new Intent(context, activity.getClass()));
                    loadInterstitial();
                }
                mInterstitialAd.setAdListener(
                        new AdListener() {
                            @Override
                            public void onAdLoaded() {
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                            }

                            @Override
                            public void onAdClosed() {
                                startActivity(new Intent(context, activity.getClass()));
                                loadInterstitial();
                            }
                        });
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, activity.getClass()));

        }
    }

    public void loadInterstitial() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void adView(final AdView adView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        if (!adView.isLoading())
            adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError var1) {
                adView.setVisibility(View.GONE);
            }

        });
    }
}
