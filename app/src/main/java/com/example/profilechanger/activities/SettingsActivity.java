package com.example.profilechanger.activities;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.profilechanger.R;
import com.example.profilechanger.adapters.NotificationSoundsAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.notification.NotificationSounds;
import com.google.android.material.switchmaterial.SwitchMaterial;


public class SettingsActivity extends AppCompatActivity implements SendDataWithKey {

    private NotificationSounds notificationSounds;
    private MyPreferences preferences;
    private TextView notiName_tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        notificationSounds = new NotificationSounds(this);
        preferences = new MyPreferences(this);
        notiName_tv = findViewById(R.id.notiName_tv);


        LinearLayout settingsNotification_LL = findViewById(R.id.settingsNotification_LL);
        SwitchMaterial settingVibrate_mSwitch = findViewById(R.id.settingVibrate_mSwitch);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton dayTheme_mRb = findViewById(R.id.dayTheme_mRb);
        RadioButton nightTheme_mRb = findViewById(R.id.nightTheme_mRb);
        RadioButton autoTheme_mRb = findViewById(R.id.autoTheme_mRb);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 0) {
                    preferences.setDataString(MyAnnotations.THEME, MyAnnotations.DAY);

                } else if (checkedId == 1) {
                    preferences.setDataString(MyAnnotations.THEME, MyAnnotations.NIGHT);

                } else if (checkedId == 2) {
                    preferences.setDataString(MyAnnotations.THEME, MyAnnotations.AUTO_CHANGE);

                }
            }
        });

        settingsNotification_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationSoundsFun();
            }
        });


        String soundName = preferences.getString(MyAnnotations.NOTIFICATION_SOUND_NAME,
                notificationSounds.getNotificationSoundsName().get(1));
        final boolean[] isVibrate = {preferences.getBoolean(MyAnnotations.NOTIFICATION_VIBRATE,
                false)};
        String theme = preferences.getString(MyAnnotations.THEME, MyAnnotations.DAY);

        settingVibrate_mSwitch.setChecked(isVibrate[0]);
        notiName_tv.setText(soundName);
        if (theme.matches(MyAnnotations.DAY)) {
            dayTheme_mRb.setChecked(true);
        } else if (theme.matches(MyAnnotations.NIGHT)) {
            nightTheme_mRb.setChecked(true);

        } else {
            autoTheme_mRb.setChecked(true);

        }

        settingVibrate_mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVibrate[0]) {
                    preferences.setBoolean(MyAnnotations.NOTIFICATION_VIBRATE, false);
                    settingVibrate_mSwitch.setChecked(false);
                    isVibrate[0] = false;

                } else {
                    preferences.setBoolean(MyAnnotations.NOTIFICATION_VIBRATE, true);
                    settingVibrate_mSwitch.setChecked(true);
                    isVibrate[0] = true;

                }


            }
        });


    }


    private void notificationSoundsFun() {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.fragment_notification_sounds_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();


        RecyclerView notificationSoundRecyclerView =
                view.findViewById(R.id.notificationSoundRecyclerView);

        TextView notification_sound_cancelBtn = view.findViewById(R.id.cancelTv);

        notification_sound_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        notification_sound_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
}
