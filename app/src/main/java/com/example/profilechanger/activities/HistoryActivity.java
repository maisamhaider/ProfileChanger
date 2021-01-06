package com.example.profilechanger.activities;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.HistoryFragmentsAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.HistoryModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        HistoryFragmentsAdapter adapter =
                new HistoryFragmentsAdapter( HistoryActivity.this
                        , getSupportFragmentManager(), 1);
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    public ArrayList<HistoryModel> getHistory(boolean done) {
        ArrayList<HistoryModel> doneList = new ArrayList<>();
        ArrayList<HistoryModel> unDoneList = new ArrayList<>();

        Cursor cursor = new MyDatabase( HistoryActivity.this).retrieveLocation();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String latitude = cursor.getString(2);
            String longitude = cursor.getString(3);
            String circleSize = cursor.getString(4);
            String geofenceType = cursor.getString(5);
            String formattedTime = cursor.getString(6);
            String expirationTime = cursor.getString(7);
            String state = cursor.getString(8);
            String dateNow = cursor.getString(9);

            if (state.matches(MyAnnotations.UN_DONE)) {
                unDoneList.add(new HistoryModel(Integer.parseInt(id),
                        title,
                        latitude,
                        longitude,
                        circleSize,
                        Long.parseLong(expirationTime),
                        state,formattedTime,geofenceType,dateNow));
            } else {
                doneList.add(new HistoryModel(Integer.parseInt(id),
                        title,
                        latitude,
                        longitude,
                        circleSize,
                        Long.parseLong(expirationTime),
                        state,formattedTime,geofenceType,dateNow));
            }

        }
        if (done) {
            return doneList;
        } else
            return unDoneList;
    }
}