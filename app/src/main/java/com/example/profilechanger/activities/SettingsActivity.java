
package com.example.profilechanger.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.profilechanger.R;
import com.example.profilechanger.adapters.NotificationSoundsAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.notification.NotificationSounds;
import com.example.profilechanger.utils.TimeUtil;
import com.google.android.gms.ads.AdView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;


public class SettingsActivity extends BaseActivity implements SendDataWithKey {

    private NotificationSounds notificationSounds;
    private MyPreferences preferences;
    private TextView notiName_tv;
    private boolean firstRun = false;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        notificationSounds = new NotificationSounds(this);
        preferences = new MyPreferences(this);
        notiName_tv = findViewById(R.id.notiName_tv);


        ConstraintLayout settingsNotification_cl = findViewById(R.id.settingsNotification_cl);
        SwitchMaterial settingVibrate_mSwitch = findViewById(R.id.settingVibrate_mSwitch);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton dayTheme_mRb = findViewById(R.id.dayTheme_mRb);
        RadioButton nightTheme_mRb = findViewById(R.id.nightTheme_mRb);
//        RadioButton autoTheme_mRb = findViewById(R.id.autoTheme_mRb);

        findViewById(R.id.profileBack_iv).setOnClickListener(v -> onBackPressed());
        AdView adView = findViewById(R.id.adView);
        adView(adView);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (firstRun) {
                switch (checkedId) {
                    case R.id.dayTheme_mRb:
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            setTheme(R.style.Theme_ProfileChanger);
                        }
                        preferences.setDataString(MyAnnotations.THEME, MyAnnotations.DAY);
                        preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
                        preferences.setBoolean(MyAnnotations.IS_THEME_CHANGE, true);

                        break;
                    case R.id.nightTheme_mRb:
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            setTheme(R.style.Theme_ProfileChanger_dark);
                        }

                        preferences.setDataString(MyAnnotations.THEME, MyAnnotations.NIGHT);
                        preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, false);
                        preferences.setBoolean(MyAnnotations.IS_THEME_CHANGE, true);
                        break;
//                        case R.id.autoTheme_mRb:
//                            preferences.setDataString(MyAnnotations.THEME, MyAnnotations.AUTO_CHANGE);
//                            TimeUtil timeUtil = new TimeUtil(SettingsActivity.this);
//                            long pm7 = timeUtil.getMillisFromFormattedDate(
//                                    timeUtil.getTimePlusHours(19)
//                                    , MyAnnotations.DEFAULT_TIME_FORMAT);
//                            long am7 = timeUtil.getMillisFromFormattedDate(
//                                    timeUtil.getTimePlusHours(7)
//                                    , MyAnnotations.DEFAULT_TIME_FORMAT);
//
//                            long current = timeUtil.getMillisFromFormattedDate(
//                                    timeUtil.getCurrentFormattedTime(),
//                                    MyAnnotations.DEFAULT_TIME_FORMAT);
//
//                            if (current > am7 && current < pm7) {
//                                //day
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                                    setTheme(R.style.Theme_ProfileChanger);
//                                } else {
//                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                                }
//                                preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, true);
//
//                            } else {//night
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                                    setTheme(R.style.Theme_ProfileChanger_dark);
//                                } else {
//                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                                }
//                                preferences.setBoolean(MyAnnotations.IS_LIGHT_THEME, false);
//                            }
//
//                            break;
                }
                recreate();
            }
        });

        settingsNotification_cl.setOnClickListener(v -> notificationSoundsFun());


        String soundName = preferences.getString(MyAnnotations.NOTIFICATION_SOUND_NAME,
                notificationSounds.getNotificationSoundsName().get(1));
        final boolean[] isVibrate = {preferences.getBoolean(MyAnnotations.NOTIFICATION_VIBRATE,
                false)};
        String theme = preferences.getString(MyAnnotations.THEME, MyAnnotations.DAY);

        settingVibrate_mSwitch.setChecked(isVibrate[0]);
        notiName_tv.setText(soundName);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (theme.matches(MyAnnotations.DAY)) {
                dayTheme_mRb.setChecked(true);
            } else if (theme.matches(MyAnnotations.NIGHT)) {
                nightTheme_mRb.setChecked(true);

            }
        } else {
            radioGroup.setVisibility(View.GONE);
        }


        firstRun = true;


        settingVibrate_mSwitch.setOnClickListener(v -> {
            if (isVibrate[0]) {
                preferences.setBoolean(MyAnnotations.NOTIFICATION_VIBRATE, false);
                settingVibrate_mSwitch.setChecked(false);
                isVibrate[0] = false;
            } else {
                preferences.setBoolean(MyAnnotations.NOTIFICATION_VIBRATE, true);
                settingVibrate_mSwitch.setChecked(true);
                isVibrate[0] = true;
            }
        });


    }

    public long get12AmMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM, 1);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }

    private void notificationSoundsFun() {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.fragment_notification_sounds_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        RecyclerView notificationSoundRecyclerView =
                view.findViewById(R.id.notificationSoundRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        notificationSoundRecyclerView.setLayoutManager(linearLayoutManager);
        NotificationSoundsAdapter notificationSoundsAdapter;

        notificationSoundsAdapter = new NotificationSoundsAdapter(SettingsActivity.this,
                notificationSounds.getNotificationSoundsName(),
                notificationSounds.getNotificationSoundsPath(), this);
        notificationSoundRecyclerView.setAdapter(notificationSoundsAdapter);
        notificationSoundsAdapter.notifyDataSetChanged();


    }


    @Override
    public void data(String key, String data, String title) {
        preferences.setDataString(MyAnnotations.NOTIFICATION_SOUND_PATH, data);
        preferences.setDataString(MyAnnotations.NOTIFICATION_SOUND_NAME, title);
        notiName_tv.setText(title);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        finish();

    }
}
