package com.example.profilechanger.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.ProfilerAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.models.ProfilerModel;

import java.util.ArrayList;

public class TimeBaseProfilerActivity extends AppCompatActivity {

    private MyDatabase database;
    private RecyclerView timeBaseProfiler_rv;
    private ArrayList<ProfilerModel> list;
    TextView noData_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_base_profiler);
        database = new MyDatabase(this);
        timeBaseProfiler_rv = findViewById(R.id.timeBaseProfiler_rv);
         noData_tv= findViewById(R.id.noData_mTv);
        Button addNew_mBtn = findViewById(R.id.addNew_mBtn);

        addNew_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 startActivity(new Intent(TimeBaseProfilerActivity.this,
                        TimeBaseProfilerEditActivity.class).putExtra(MyAnnotations.IS_UPDATE,
                        false)
                );

            }
        });
    }

    public void load() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        int size = list.size();
        ProfilerAdapter adapter = new ProfilerAdapter(this, list, true,database);
        timeBaseProfiler_rv.setAdapter(adapter);
        timeBaseProfiler_rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    public void addDataInList() {
        list = new ArrayList<>();
        ProfilerModel model;
        Cursor cursor = database.retrieveTimeTable();
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
        if (list.isEmpty())
        {
            noData_tv.setVisibility(View.VISIBLE);
        }
        else
        {
            noData_tv.setVisibility(View.GONE);

        }
    }


}