package com.example.profilechanger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.ProfilerAdapter;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.ProfilerModel;

import java.util.ArrayList;

public class LocationBaseProfilerActivity extends AppCompatActivity {
    private MyDatabase database;
    private RecyclerView locationBaseProfiler_rv;
    private ArrayList<ProfilerModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_base_profiler);
        database = new MyDatabase(this);
        locationBaseProfiler_rv = findViewById(R.id.locationBaseProfiler_rv);
        TextView noData_tv = findViewById(R.id.noData_mTv);
        Button addNew_mBtn = findViewById(R.id.addNew_mBtn);
        addDataInList();
        load();
        if (list.isEmpty()) {
            noData_tv.setVisibility(View.VISIBLE);
        } else {
            noData_tv.setVisibility(View.GONE);

        }
        addNew_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void load() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        ProfilerAdapter adapter = new ProfilerAdapter(this, list, false, database);
        locationBaseProfiler_rv.setLayoutManager(layoutManager);
        locationBaseProfiler_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addDataInList() {
        list = new ArrayList<>();
        ProfilerModel model;
        Cursor cursor = database.retrieveLocation();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                model = new ProfilerModel();
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                model.setId(id);
                model.setName(title);
                list.add(model);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDataInList();
        load();
    }
}