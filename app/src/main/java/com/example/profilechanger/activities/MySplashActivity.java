package com.example.profilechanger.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class MySplashActivity extends AppCompatActivity {
    boolean isTermAccepted = false;
    View splash, termAndCondition;
    private Button decline;
    private Button accept;
    private CheckBox termAndCondition_Cb;
    MyPreferences preferences;
    InterstitialAd mInterstitialAd;
    //    View help_main_layout;
    boolean helpFirstTime = true;
    ImageView backgroundIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial));
        loadInterstitial();

        splash = findViewById(R.id.splash);
        termAndCondition = findViewById(R.id.termAndCondition);
//        help_main_layout = findViewById(R.id.help_main_layout);
        decline = findViewById(R.id.decline_btn);
        accept = findViewById(R.id.accept_btn);
        termAndCondition_Cb = findViewById(R.id.termAndCondition_Cb);
        preferences = new MyPreferences(MySplashActivity.this);
        isTermAccepted = preferences.getBoolean(MyAnnotations.IS_TERMS_CONDITION, false);

//        helpFirstTime = preferences.getBoolean(MyAnnotations.HELP_FIRST_TIME, true);

        backgroundIv = findViewById(R.id.backgroundIv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    backgroundIv.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_splash_dark));
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    backgroundIv.setImageDrawable(ContextCompat.getDrawable(this,
                            R.drawable.ic_splash_light));
                    break;
            }
        } else {
            if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, true)) {

                backgroundIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_splash_light));
            } else {
                backgroundIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_splash_dark));

            }
        }

        Load_withAds();
    }


    public void loadFun() {


        if (isTermAccepted) {

//            splash.setVisibility(View.GONE);
//            termAndCondition.setVisibility(View.GONE);
//            if (helpFirstTime) {
//                help_main_layout.setVisibility(View.VISIBLE);
//             } else
            startActivity(new Intent(MySplashActivity.this, MainActivity.class));


        } else {
            splash.setVisibility(View.GONE);
            termAndCondition.setVisibility(View.VISIBLE);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (termAndCondition_Cb.isChecked()) {
                        preferences.setBoolean(MyAnnotations.IS_TERMS_CONDITION, true);
                        termAndCondition.setVisibility(View.GONE);
//                        help_main_layout.setVisibility(View.VISIBLE);
//                        helpScreen();
                        startActivity(new Intent(MySplashActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(MySplashActivity.this,
                                "Please check the terms and conditions box",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });


        }

    }

    public void Load_withAds() {
        try {
//            mDialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {

                    loadFun();
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
                                loadFun();
                                loadInterstitial();
                            }
                        });
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
            loadFun();
        }
    }


    public void loadInterstitial() {
        // Request a new ad if one isn't already loaded
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

//    public void helpScreen() {
//
//        ViewPager viewPager =   findViewById(R.id.helpViewPager);
//        viewPager.setAdapter(new HelpPagerAdapter(this));
//
//        TextView next_tv = findViewById(R.id.next_tv);
//        TextView help_one_tv = findViewById(R.id.help_one_tv);
//        TextView help_two_tv = findViewById(R.id.help_two_tv);
//        TextView help_three_tv = findViewById(R.id.help_three_tv);
//        TextView back_tv = findViewById(R.id.back_tv);
//        back_tv.setVisibility(View.INVISIBLE);
//        //initial stage
//        final int[] screen = {0};
//        help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                R.drawable.black_circle_bg, null));
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {
//                    back_tv.setVisibility(View.INVISIBLE);
//                    next_tv.setText(MyAnnotations.next);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    screen[0] = 0;
//
//                } else if (position == 1) {
//                    screen[0] = 1;
//                    back_tv.setVisibility(View.VISIBLE);
//                    next_tv.setText(MyAnnotations.next);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//
//
//                } else {
//                    back_tv.setVisibility(View.VISIBLE);
//                    screen[0] = 2;
//                    next_tv.setText(MyAnnotations.done);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    // after action
//        next_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (screen[0] == 0) {
//                    viewPager.setCurrentItem(1);
//                    back_tv.setVisibility(View.VISIBLE);
//                    screen[0] = 1;
//                    next_tv.setText(MyAnnotations.next);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//
//                } else if (screen[0] == 1) {
//                    viewPager.setCurrentItem(2);
//
//                    screen[0] = 2;
//                    next_tv.setText(MyAnnotations.done);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//
//                } else {
//                    if (next_tv.getText().toString().matches(MyAnnotations.done)) {
//                        preferences.setBoolean(MyAnnotations.HELP_FIRST_TIME, false);
//                        startActivity(new Intent(profile.manager.location.based.auto.profile.changer.activities.MySplashActivity.this, MainActivity.class));
//                    }
//                }
//            }
//        });
//        back_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (screen[0] == 2) {
//                    screen[0] = 1;
//                    viewPager.setCurrentItem(1);
//
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//
//
//                } else if (screen[0] == 1) {
//                    screen[0] = 0;
//                    viewPager.setCurrentItem(0);
//                    back_tv.setVisibility(View.INVISIBLE);
//                    help_one_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.black_circle_bg, null));
//                    help_two_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//                    help_three_tv.setBackground(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.trans_black_circle_bg, null));
//
//
//                }
//            }
//        });
//    }
}