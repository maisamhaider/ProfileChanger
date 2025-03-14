package com.example.profilechanger.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.MainFragmentsAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.annotations.PermissionCodes;
import com.example.profilechanger.database.MyDatabase;

import com.example.profilechanger.permissions.Permissions;
import com.example.profilechanger.services.Service1;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.google.android.material.tabs.TabLayout;


import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

public class MainActivity extends BaseActivity {

    private Permissions permissions;
    NotificationManager mNotificationManager;
    MyPreferences preferences;
    MyDatabase database;
    ViewPager viewPager;
    AlertDialog dialog;
    boolean click = false;
    View include;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions = new Permissions(this);
        database = new MyDatabase(this);
        preferences = new MyPreferences(this);
        MainFragmentsAdapter adapter =
                new MainFragmentsAdapter(MainActivity.this
                        , getSupportFragmentManager(), 1);
        viewPager = findViewById(R.id.mainViewPager);
        TabLayout tabLayout = findViewById(R.id.mainTabLayout);
        include = findViewById(R.id.include);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ImageView mainHeaderIv = findViewById(R.id.mainHeaderIv);
        View people_pad = findViewById(R.id.menu_view);
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);


        if (!preferences.getBoolean(MyAnnotations.PRE_PROFILES_LOADED, false)) {
            insertPreDefinedProfiles();
            preferences.setBoolean(MyAnnotations.PRE_PROFILES_LOADED, true);
        }

        if (preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, false)) {

            mainHeaderIv.setImageDrawable(ContextCompat
                    .getDrawable(this, R.drawable.ic_header_light));
            people_pad.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.shape_1_light));
        } else {
            mainHeaderIv.setImageDrawable(ContextCompat
                    .getDrawable(this, R.drawable.ic_header_dark));
            people_pad.setBackground(ContextCompat.getDrawable(
                    this, R.drawable.shape_1_dark));

        }
        ImageView addNewProfile_mIv = findViewById(R.id.addNewProfile_mIv);

        addNewProfile_mIv.setOnClickListener(v ->
        {
            if (!click) {
                click = true;
                Load_withAds(MainActivity.this, new ProfilesActivity());
            }
        });

        findViewById(R.id.settingsIv).setOnClickListener(v -> {
            if (!click) {
                click = true;
                MotionLayout motionLayout = findViewById(R.id.motionLayout);
                motionLayout.transitionToEnd();
                Load_withAds(MainActivity.this, new SettingsActivity());
            }

        });
        findViewById(R.id.rateUsIv).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "rateUsIv", Toast.LENGTH_SHORT).show();
            rateUs();

        });
        findViewById(R.id.moreAppsIv).setOnClickListener(v -> Toast.makeText(
                MainActivity.this, "moreAppsIv", Toast.LENGTH_SHORT).show());
        findViewById(R.id.shareIv).setOnClickListener(v -> shareUs());
        findViewById(R.id.infoIv).setOnClickListener(v -> dialog());
        findViewById(R.id.helpIv).setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show());


        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;

//        if (manufacturer.toLowerCase().matches("huawei")
//                && model.toLowerCase().matches("stk-l21")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(MainActivity.this, Service1.class));
            } else {
                startService(new Intent(MainActivity.this, Service1.class));
            }
//        }

    }

    public void insertPreDefinedProfiles() {
        database.insertProfile(getResources().getString(R.string.general),
                MyAnnotations.RINGER_MODE_NORMAL,
                "90", "90", "90", "90",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.loud),
                MyAnnotations.RINGER_MODE_NORMAL,
                "100", "100", "100", "100",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.silent),
                MyAnnotations.RINGER_MODE_SILENT,
                "0", "0", "0", "0",
                MyAnnotations.OFF, MyAnnotations.OFF, MyAnnotations.OFF);

        database.insertProfile(getResources().getString(R.string.medium),
                MyAnnotations.RINGER_MODE_NORMAL,
                "60", "60", "60", "60",
                MyAnnotations.ON, MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.low),
                MyAnnotations.RINGER_MODE_NORMAL,
                "40", "40", "40", "40", MyAnnotations.ON,
                MyAnnotations.ON, MyAnnotations.ON);

        database.insertProfile(getResources().getString(R.string.vibrate),
                MyAnnotations.RINGER_MODE_VIBRATE,
                "0", "0", "0", "0", MyAnnotations.ON,
                MyAnnotations.OFF, MyAnnotations.OFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
        click = false;

        if (!permissions.locationPer()) {

            startActivity(new Intent(MainActivity.this, PermissionActivity.class));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.permission))
                        .setCancelable(false)
                        .setMessage(getString(R.string.write_settings_alert_text))
                        .setPositiveButton(getString(R.string.allow),
                                (dialog, which) -> {
                                    permissions.openAndroidPermissionsMenu(MainActivity.this);
                                    dialog.dismiss();
                                }).setNegativeButton(R.string.decline,
                        (dialog, which) ->
                                MainActivity.this.finishAffinity());

                dialog = builder.create();
                dialog.show();
            } else if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                permissions.doNoDisturbPermissionDialog();

            } else if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.permission))
                        .setCancelable(false)
                        .setMessage(getString(R.string.write_settings_alert_text))
                        .setPositiveButton(getString(R.string.allow),
                                (dialog, which) -> {
                                    permissions.overlayAppPermission();
                                    dialog.dismiss();
                                }).setNegativeButton(R.string.decline,
                        (dialog, which) ->
                                MainActivity.this.finishAffinity());

                dialog = builder.create();
                dialog.show();

            } else {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("App protect").setMessage(R.string.huawei_text)
                        .setCancelable(false)
                        .setPositiveButton(R.string.settings,*/
                /*(dialogInterface, i) -> {*/
                checkOptimization();
                               /*     preferences.setBoolean("protected", true);
                                }).create().show();*/

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PermissionCodes.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) {
            if (resultCode != RESULT_OK) {
                finishAffinity();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkOptimization() {
        String packageName = getApplicationContext().getPackageName();
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (pm != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent,
                            PermissionCodes.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);

                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void dialog() {
        View view = getLayoutInflater().inflate(R.layout.about_dialog_layout, null,
                false);
        TextView aboutUsCloseApp_tv = view.findViewById(R.id.aboutUsCloseApp_tv);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true).setView(view);

        final android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView version_tv = view.findViewById(R.id.appVersion_tv);

        aboutUsCloseApp_tv.setOnClickListener(v -> {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        });

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            version_tv.setText(version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id="
                        + new PackageInfo().packageName)));
    }

    public void shareUs() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id="
                        + new PackageInfo().packageName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void exit() {
        try {
            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(this);
            LayoutInflater layoutInflater = getLayoutInflater();
            @SuppressLint("InflateParams") final View dialogView =
                    layoutInflater.inflate(R.layout.exit_layout, null);
            ConstraintLayout yes_cl = dialogView.findViewById(R.id.yes_cl);
            ConstraintLayout no_cl = dialogView.findViewById(R.id.no_cl);
            ConstraintLayout rateUs_cl = dialogView.findViewById(R.id.rateUs_cl);


            builder.setView(dialogView);
            final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            yes_cl.setOnClickListener(view -> {
                alertDialog.cancel();
                MainActivity.this.finishAffinity();
            });

            no_cl.setOnClickListener(view -> alertDialog.dismiss());
            rateUs_cl.setOnClickListener(view -> MainActivity.this.rateUs()
            );

        } catch (Exception a) {
            a.printStackTrace();
        }
    }


}