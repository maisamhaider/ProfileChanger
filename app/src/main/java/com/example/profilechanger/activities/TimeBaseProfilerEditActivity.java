package com.example.profilechanger.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;

public class TimeBaseProfilerEditActivity extends AppCompatActivity {

    Button save_mBtn, delete_mBtn;
    EditText timeBaseProfiler_mEt;
    TextView startTime_mtv,startProfiler_mtv,endTime_mtv,endProfiler_mtv;
    String  profilerTitle,profileTitle,startTime,endTime,state,date,repeat,days,profileStartId,
    profileEndId;
    MyDatabase database ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_base_profiler_dialog_layout);
        database = new MyDatabase(this);
        String id = getIntent().getStringExtra(MyAnnotations.PROFILE_ID);
        boolean isUpdate = getIntent().getBooleanExtra(MyAnnotations.IS_UPDATE, false);
        timeBaseProfiler_mEt = findViewById(R.id.timeBaseProfiler_mEt);
        save_mBtn = findViewById(R.id.save_mBtn);
        delete_mBtn = findViewById(R.id.delete_mBtn);
        startTime_mtv = findViewById(R.id.startTime_mtv);
        startProfiler_mtv = findViewById(R.id.startProfiler_mtv);
        endTime_mtv = findViewById(R.id.endTime_mtv);
        endProfiler_mtv = findViewById(R.id.endProfiler_mtv);

        if (isUpdate) {
            save_mBtn.setText(getResources().getString(R.string.update));
            delete_mBtn.setText(getResources().getString(R.string.delete));

        } else
        {
            save_mBtn.setText(getResources().getString(R.string.save));
            delete_mBtn.setText(getResources().getString(R.string.cancel));

        }

        save_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilerTitle = timeBaseProfiler_mEt.getText().toString();

                if (isUpdate)
                {
                    database.updateTimeTable(id,profilerTitle,profileTitle,startTime,endTime,state,
                            date,repeat,days,profileStartId,profileEndId);
                }
                else
                {
                    database.insertTimeTable(profilerTitle,profileTitle,startTime,endTime,state,
                        date,repeat,days,profileStartId,profileEndId);
                }

            }
        });
        delete_mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdate) {
                    database.deleteTimeTable(id);
                } else
                {
                    finish();
                }

            }
        });
    }

    public void loadProfileFromDb(String id) {
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            profilerTitle = cursor.getString(1);
            profileTitle = cursor.getString(2);
            startTime = cursor.getString(3);
            endTime = cursor.getString(4);
            state = cursor.getString(5);
            date = cursor.getString(6);
            repeat = cursor.getString(7);
            days = cursor.getString(8);
            profileStartId = cursor.getString(9);
            profileEndId = cursor.getString(10);

        }
    }
}
