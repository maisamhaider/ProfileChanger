package com.example.profilechanger.activities;

import androidx.annotation.NonNull;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.PermissionCodes;
import com.example.profilechanger.permissions.Permissions;

public class MainActivity extends BaseActivity {

    private Permissions permissions;
    NotificationManager mNotificationManager;
    View main_activity_buttons_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions = new Permissions(this);
        permissions.permission();
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
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
                intent(new ProfilesActivity());
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
                intent(new ProfilesActivity());
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