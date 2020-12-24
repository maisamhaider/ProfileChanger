package com.example.profilechanger.activities;

import androidx.annotation.NonNull;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.annotations.PermissionCodes;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.TimeBaseProfiler;
import com.example.profilechanger.permissions.Permissions;
import com.example.profilechanger.sharedpreferences.MyPreferences;

public class MainActivity extends BaseActivity {

    private Permissions permissions;
    NotificationManager mNotificationManager;
    View main_activity_buttons_view;
    MyPreferences preferences;
    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions = new Permissions(this);
        database = new MyDatabase(this);
        preferences = new MyPreferences(this);

        permissions.permission();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            openPermissionsMenu();

        }

        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        boolean isFirstTime = preferences.getBoolean(MyAnnotations.PRE_PROFILES_LOADED,
                false);
        if (!isFirstTime) {
            insertPreDefinedProfiles();
            preferences.setBoolean(MyAnnotations.PRE_PROFILES_LOADED, true);
        }


        Button profiles_mBtn = findViewById(R.id.profiles_mBtn);
        Button locationBasePro_mBtn = findViewById(R.id.locationBasePro_mBtn);
        Button timeBasePro_mBtn = findViewById(R.id.timeBasePro_mBtn);
        Button rateUs_mBtn = findViewById(R.id.rateUs_mBtn);
        Button setting_mBtn = findViewById(R.id.setting_mBtn);

        profiles_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(new ProfilesActivity());
            }
        });

        locationBasePro_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(new ProfilesActivity());
            }
        });
        timeBasePro_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(new TimeBaseProfilerActivity());
            }
        });
        rateUs_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(new ProfilesActivity());
            }
        });
        setting_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent(new SettingsActivity());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == PermissionCodes.REQ_CODE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                    this.permissions.doNoDisturbPermissionDialog();
                }
            }

        } else if (requestCode == PermissionCodes.REQ_CODE &&
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                    this.permissions.doNoDisturbPermissionDialog();
                }

            }
        }

    }

    private void openPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.ACTION_MANAGE_WRITE_SETTINGS == )
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    public void insertPreDefinedProfiles() {
        database.insertProfile(getResources().getString(R.string.general),
                MyAnnotations.RINGER_MODE_NORMAL,
                "90", "90", "90", "90",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.loud),
                MyAnnotations.RINGER_MODE_NORMAL,
                "100", "100", "100", "100",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.silent),
                MyAnnotations.RINGER_MODE_SILENT,
                "0", "0", "0", "0",
                MyAnnotations.OFF, MyAnnotations.OFF, MyAnnotations.OFF, MyAnnotations.OFF);

        database.insertProfile(getResources().getString(R.string.medium),
                MyAnnotations.RINGER_MODE_NORMAL,
                "60", "60", "60", "60",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.low),
                MyAnnotations.RINGER_MODE_NORMAL,
                "40", "40", "40", "40", MyAnnotations.ON,
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.vibrate),
                MyAnnotations.RINGER_MODE_VIBRATE,
                "0", "0", "0", "0", MyAnnotations.ON,
                MyAnnotations.OFF, MyAnnotations.OFF, MyAnnotations.OFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
//                this.permissions.doNoDisturbPermissionDialog();
//            }
//
//        }
    }
}