package com.example.profilechanger.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.MainFragmentsAdapter;
import com.example.profilechanger.adapters.PermissionsFragmentsAdapter;
import com.example.profilechanger.annotations.PermissionCodes;
import com.google.android.material.tabs.TabLayout;

public class PermissionActivity extends AppCompatActivity {
    ViewPager permission_vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        PermissionsFragmentsAdapter adapter =
                new PermissionsFragmentsAdapter(this
                        , getSupportFragmentManager(), 1);
         permission_vp = findViewById(R.id.permission_vp);
        permission_vp.setAdapter(adapter);
     }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PermissionCodes.REQ_CODE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            finish();
        }
        else
        {
            finishAffinity();
        }
    }
}