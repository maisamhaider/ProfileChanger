package com.example.profilechanger.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profilechanger.R;
import com.example.profilechanger.adapters.ProfilesAdapter;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.annotations.NoAnnotation;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.interfaces.SendDataWithKey;
import com.example.profilechanger.models.ProfilesModel;
import com.example.profilechanger.utils.AlarmClass;
import com.example.profilechanger.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeBaseProfilerEditActivity extends BaseActivity implements SendDataWithKey,
        View.OnClickListener {

    private EditText timeBaseProfiler_mEt;
    private TextView startDate_mtv;
    private TextView startProfile_mtv;
    private TextView endTime_mtv;
    private TextView endProfile_mtv;

    private String profilerTitle, startDate, startTime, endDate,
            endTime, state = MyAnnotations.UN_DONE, date, repeat = MyAnnotations.OFF,
            days = "", profileStartId,
            profileEndId;
    private String sa, su, mo, tu, we, th, fr;
    private CheckBox sa_mCb, su_mCb, mo_mCb, tu_mCb, we_mCb, th_mCb, fr_mCb, allDays_mCb;
    private String profileStartTitle, profileEndTitle;
    private final Calendar calendar = Calendar.getInstance();
    private boolean isstartDate_mtv = false;
    private long startDateAndTime = 0;
    private long endDateAndTime = 0;
    private long startTimeLong = 0;
    private long endTimeLong = 0;
    private AlertDialog dialog;
    private DatePickerDialog dP;
    private String id;

    private MyDatabase database;
    private TimeUtil timeUtil;
    private AlarmClass alarmClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_base_profiler_dialog_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        database = new MyDatabase(this);
        timeUtil = new TimeUtil(this);
        alarmClass = new AlarmClass(this);

        id = getIntent().getStringExtra(MyAnnotations.TIME_PROFILER_ID);
        boolean isUpdate = getIntent().getBooleanExtra(MyAnnotations.IS_UPDATE, false);
        timeBaseProfiler_mEt = findViewById(R.id.timeBaseProfiler_mEt);
        Button save_mBtn = findViewById(R.id.save_mBtn);
        Button delete_mBtn = findViewById(R.id.delete_mBtn);
        ConstraintLayout startDate_cl, endDate_cl, startProfile_cl, endProfile_cl;


        startDate_cl = findViewById(R.id.startDate_cl);
        endDate_cl = findViewById(R.id.endDate_cl);
        startProfile_cl = findViewById(R.id.startProfile_cl);
        endProfile_cl = findViewById(R.id.endProfile_cl);

        startDate_mtv = findViewById(R.id.startDate_mtv);
        startProfile_mtv = findViewById(R.id.startProfiler_mtv);
        endTime_mtv = findViewById(R.id.endTime_mtv);
        endProfile_mtv = findViewById(R.id.endProfiler_mtv);
        TextView date_mTv = findViewById(R.id.date_mTv);

        sa_mCb = findViewById(R.id.sa_mCb);
        su_mCb = findViewById(R.id.su_mCb);
        mo_mCb = findViewById(R.id.mo_mCb);
        tu_mCb = findViewById(R.id.tu_mCb);
        we_mCb = findViewById(R.id.we_mCb);
        th_mCb = findViewById(R.id.th_mCb);
        fr_mCb = findViewById(R.id.fr_mCb);
        allDays_mCb = findViewById(R.id.allDays_mCb);

        sa = getResources().getString(R.string.sat);
        su = getResources().getString(R.string.sun);
        mo = getResources().getString(R.string.mon);
        tu = getResources().getString(R.string.tue);
        we = getResources().getString(R.string.wed);
        th = getResources().getString(R.string.thu);
        fr = getResources().getString(R.string.fri);


        sa_mCb.setOnClickListener(this);
        su_mCb.setOnClickListener(this);
        mo_mCb.setOnClickListener(this);
        tu_mCb.setOnClickListener(this);
        we_mCb.setOnClickListener(this);
        th_mCb.setOnClickListener(this);
        fr_mCb.setOnClickListener(this);
        allDays_mCb.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    checkBoxesDarkLight(true);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    checkBoxesDarkLight(false);

                    break;
            }
        } else {
            checkBoxesDarkLight(!preferences.getBoolean(MyAnnotations.IS_LIGHT_THEME, true));
        }

        findViewById(R.id.prrBack_iv).setOnClickListener(v -> finish());


        startProfile_cl.setOnClickListener(v -> profileDialog(MyAnnotations.START_PROFILE_ID));
        endProfile_cl.setOnClickListener(v -> profileDialog(MyAnnotations.END_PROFILE_ID));
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog tP = new TimePickerDialog(TimeBaseProfilerEditActivity.this,
                timePicker, hour, minutes, false);

        startDate_cl.setOnClickListener(v -> {
            tP.show();
            isstartDate_mtv = true;
        });
        endDate_cl.setOnClickListener(v -> {
            tP.show();
            isstartDate_mtv = false;
        });
        tP.setOnCancelListener(dialog -> {
            if (isstartDate_mtv) {
                startTime = timeUtil.getFormattedTimeJust(System.currentTimeMillis());
            } else {
                endTime = timeUtil.getFormattedTimeJust(System.currentTimeMillis() +
                        DateUtils.HOUR_IN_MILLIS);
            }
        });

        if (isUpdate) {
            loadProfileFromDb(id);
            save_mBtn.setText(getResources().getString(R.string.update));
            delete_mBtn.setText(getResources().getString(R.string.delete));
            timeBaseProfiler_mEt.setText(profilerTitle);

            sa_mCb.setChecked(days.contains(MyAnnotations.SAT));

            su_mCb.setChecked(days.contains(MyAnnotations.SUN));

            mo_mCb.setChecked(days.contains(MyAnnotations.MON));

            tu_mCb.setChecked(days.contains(MyAnnotations.TUS));

            we_mCb.setChecked(days.contains(MyAnnotations.WED));

            th_mCb.setChecked(days.contains(MyAnnotations.THU));

            fr_mCb.setChecked(days.contains(MyAnnotations.FRI));

            if (isAllCheckBoxChecked()) {
                allDays_mCb.setChecked(true);

            }


        } else {
            save_mBtn.setText(getResources().getString(R.string.save));
            delete_mBtn.setText(getResources().getString(R.string.cancel));

            startDate = timeUtil.getFormattedDateAndTime(System.currentTimeMillis());

            endDate = timeUtil.getFormattedDateAndTime(System.currentTimeMillis() +
                    DateUtils.HOUR_IN_MILLIS);
            date_mTv.setText(timeUtil.getFormattedDateAndTime(System.currentTimeMillis()));
            profileStartTitle = getResources().getString(R.string.N_A);
            profileEndTitle = getResources().getString(R.string.N_A);
            date = timeUtil.getFormattedDateAndTime(System.currentTimeMillis());


        }


        startDate_mtv.setText(startDate);
        endTime_mtv.setText(endDate);

        startProfile_mtv.setText(profileStartTitle);
        endProfile_mtv.setText(profileEndTitle);
        date_mTv.setText(date);


        save_mBtn.setOnClickListener(v -> {
            profilerTitle = timeBaseProfiler_mEt.getText().toString();
            profileStartTitle = startProfile_mtv.getText().toString();
            profileEndTitle = endProfile_mtv.getText().toString();
            startDate = startDate_mtv.getText().toString();
            endDate = endTime_mtv.getText().toString();

            startTime = startDate_mtv.getText().toString();
            endTime = endTime_mtv.getText().toString();
            if (isUpdate) {
                if (repeat.matches(MyAnnotations.OFF)) {
                    long isUpdate1 = database.updateTimeTable(id, profilerTitle, profileStartTitle,
                            profileEndTitle, startDate, endDate, state, date, repeat, days,
                            profileStartId, profileEndId);
                    if (isUpdate1 != -1) {
                        long triggerTime1 = timeUtil.getMillisFromFormattedDate(startDate,
                                MyAnnotations.DEFAULT_FORMAT);
                        alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                                Integer.parseInt(id) + 1000, false);
                        finish();
                    }

                } else {
                    long isUpdate1 = database.updateTimeTable(id, profilerTitle,
                            profileStartTitle, profileEndTitle, startTime, endTime, state,
                            date, repeat, days, profileStartId, profileEndId);

                    if (isUpdate1 != -1) {
                        //set start Alarm
                        long triggerTime1 = timeUtil.getMillisFromFormattedDate(startTime,
                                MyAnnotations.DEFAULT_TIME_FORMAT);
                        alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                                Integer.parseInt(id) + 1000, true);

                        finish();
                    }
                }

            } else {
                if (isOneFieldRemains()) {
                    Toast.makeText(TimeBaseProfilerEditActivity.this, "please fill " +
                                    "empty field",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (repeat.matches(MyAnnotations.OFF)) {

                        long isInsert = database.insertTimeTable(profilerTitle,
                                profileStartTitle, profileEndTitle, startDate, endDate, state,
                                date, repeat, days, profileStartId, profileEndId);
                        if (isInsert != -1) {
                            Toast.makeText(TimeBaseProfilerEditActivity.this,
                                    "insert", Toast.LENGTH_SHORT).show();

                            //set start Alarm
                            long triggerTime1 = timeUtil.getMillisFromFormattedDate(startDate,
                                    MyAnnotations.DEFAULT_FORMAT);

                            alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                                    (int) isInsert + 1000, false);
                            finish();
                        } else {
                            Toast.makeText(TimeBaseProfilerEditActivity.this,
                                    "not insert", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        long isInsert = database.insertTimeTable(profilerTitle, profileStartTitle,
                                profileEndTitle, startTime, endTime, state, date, repeat, days,
                                profileStartId, profileEndId);
                        if (isInsert != -1) {

                            alarmClass = new AlarmClass(TimeBaseProfilerEditActivity.this);
                            Toast.makeText(TimeBaseProfilerEditActivity.this,
                                    "insert", Toast.LENGTH_SHORT).show();

                            //set start Alarm
                            long triggerTime1 = timeUtil.getMillisFromFormattedDate(startTime,
                                    MyAnnotations.DEFAULT_TIME_FORMAT);

                            alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                                    (int) isInsert + 1000, true);


                            finish();
                        } else {
                            Toast.makeText(TimeBaseProfilerEditActivity.this,
                                    "not insert", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        delete_mBtn.setOnClickListener(v -> {
            if (isUpdate) {
                AlertDialog alertDialog = deleteDialog(id);
                alertDialog.show();
            } else {
                finish();
            }
        });

        Calendar calendar1 = Calendar.getInstance();
        final int year = calendar1.get(Calendar.YEAR);
        final int month = calendar1.get(Calendar.MONTH);
        final int day = calendar1.get(Calendar.DAY_OF_MONTH);

        dP = new DatePickerDialog(TimeBaseProfilerEditActivity.this,
                datePicker, year, month, day);
        dP.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        dP.setOnCancelListener(dialog -> {
            if (isstartDate_mtv) {
                if (startTime.isEmpty()) {
                    startDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) +
                            " " + timeUtil.getCurrentFormattedTime();
                } else {
                    startDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " "
                            + startTime;
                }
                startDate_mtv.setText(startDate);
                startDateAndTime = timeUtil.getMillisFromFormattedDate(startDate,
                        MyAnnotations.DEFAULT_FORMAT);

                if (startDateAndTime >= endDateAndTime) {
                    endDate = timeUtil.getFormattedDateAndTime(startDateAndTime +
                            DateUtils.HOUR_IN_MILLIS);
                    endTime_mtv.setText(endDate);
                }

            } else {

                if (endTime.isEmpty()) {
                    endDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " " +
                            timeUtil.getCurrentFormattedTime();

                } else {
                    endDate =
                            timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " " +
                                    endTime;
                }
                endDateAndTime = timeUtil.getMillisFromFormattedDate(endDate,
                        MyAnnotations.DEFAULT_FORMAT);
                endTime_mtv.setText(endDate);
                if (startDateAndTime >= endDateAndTime) {
                    startDate = timeUtil.getFormattedDateAndTime(endDateAndTime -
                            DateUtils.HOUR_IN_MILLIS);
                    startDate_mtv.setText(startDate);
                }

            }

            isstartDate_mtv = false;

        });
    }


    private final DatePickerDialog.OnDateSetListener datePicker =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (isstartDate_mtv) {
                        //
                        if (startTime.isEmpty()) {
                            startDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) +
                                    " " + timeUtil.getCurrentFormattedTime();
                        } else {
                            startDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " "
                                    + startTime;
                        }
                        startDate_mtv.setText(startDate);
                        startDateAndTime = timeUtil.getMillisFromFormattedDate(startDate,
                                MyAnnotations.DEFAULT_FORMAT);

                        if (startDateAndTime >= endDateAndTime) {
                            endDate = timeUtil.getFormattedDateAndTime(startDateAndTime +
                                    DateUtils.HOUR_IN_MILLIS);
                            endTime_mtv.setText(endDate);
                        }
                    } else {
                        if (endTime.isEmpty()) {
                            endDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " " +
                                    timeUtil.getCurrentFormattedTime();
                        } else {
                            endDate = timeUtil.getFormattedDate(calendar.getTimeInMillis()) + " " +
                                            endTime;
                        }
                        endDateAndTime = timeUtil.getMillisFromFormattedDate(endDate,
                                MyAnnotations.DEFAULT_FORMAT);
                        endTime_mtv.setText(endDate);
                        if (startDateAndTime >= endDateAndTime) {
                            startDate = timeUtil.getFormattedDateAndTime(endDateAndTime -
                                    DateUtils.HOUR_IN_MILLIS);
                            startDate_mtv.setText(startDate);
                        }

                    }


                }
            };

    public void checkBoxesDarkLight(boolean dark) {
        if (dark) {
            sa_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            su_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            mo_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            tu_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            we_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            th_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            fr_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
            allDays_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.white)));
        } else {

            sa_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            su_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            mo_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            tu_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            we_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            th_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            fr_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
            allDays_mCb.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this,
                    R.color.colorPrimaryVariantLight)));
        }
    }

    private final TimePickerDialog.OnTimeSetListener timePicker =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    if (isstartDate_mtv) {
                        startTime = timeUtil.getFormattedTimeJust(calendar.getTimeInMillis());
                        startDate_mtv.setText(startTime);
                        startTimeLong = timeUtil.getMillisFromFormattedDate(startTime,
                                MyAnnotations.DEFAULT_TIME_FORMAT);
                    } else {
                        endTime = timeUtil.getFormattedTimeJust(calendar.getTimeInMillis());
                        endTime_mtv.setText(endTime);
                        endTimeLong = timeUtil.getMillisFromFormattedDate(endTime,
                                MyAnnotations.DEFAULT_TIME_FORMAT);


                    }
                    if (!isOneCheckBoxChecked()) {

                        dP.show();
                    } else {
                        if (isstartDate_mtv) {
                            if (startTimeLong >= endTimeLong) {
                                endTime = timeUtil.getFormattedTimeJust(startTimeLong +
                                        DateUtils.MINUTE_IN_MILLIS*5);
//
                                endTime_mtv.setText(endTime);
                            }

                        } else
                            {
                            if (startTimeLong >= endTimeLong) {
                                startTime = timeUtil.getFormattedTimeJust(endTimeLong -
                                        DateUtils.MINUTE_IN_MILLIS*5);
//
                                startDate_mtv.setText(startTime);
                            }
                        }
                    }
                }
            };

    public void loadProfileFromDb(String id) {
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            profilerTitle = cursor.getString(1);
            profileStartTitle = cursor.getString(2);
            profileEndTitle = cursor.getString(3);
            startDate = cursor.getString(4);
            endDate = cursor.getString(5);
            state = cursor.getString(6);
            date = cursor.getString(7);
            repeat = cursor.getString(8);
            days = cursor.getString(9);
            profileStartId = cursor.getString(10);
            profileEndId = cursor.getString(11);

        }
    }

    public void profileDialog(String profile) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View view = LayoutInflater.from(this).inflate(R.layout.start_end_profile_dialog_layout,
                null, false);
        RecyclerView recyclerView = view.findViewById(R.id.startEndProfile_rv);

        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ProfilesModel> list = new ArrayList<>();
        ProfilesModel profilesModel;
        Cursor cursor = database.retrieveProfile();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                profilesModel = new ProfilesModel();
                String id = cursor.getString(0);
                String title = cursor.getString(1);

                profilesModel.setId(id);
                profilesModel.setPROFILE_TITLE(title);
                list.add(profilesModel);
            }
        }
        ProfilesAdapter profilesAdapter = new ProfilesAdapter(this, list, database,
                true, true);
        recyclerView.setAdapter(profilesAdapter);
        profilesAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view).setCancelable(true);
        profilesAdapter.setSendDataWithKey(this, profile);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    @Override
    public void data(String key, String data, String title) {
        if (key.matches(MyAnnotations.START_PROFILE_ID)) {
            profileStartId = data;
            startProfile_mtv.setText(title);


        } else {
            profileEndId = data;
            endProfile_mtv.setText(title);
        }
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }

    }

    public AlertDialog deleteDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getResources().
                getString(R.string.delete))
                .setMessage(getResources().
                        getString(R.string.delete_message))
                .setPositiveButton(getResources().getString(R.string.yes),
                        (dialog, which) -> {
                            database.deleteTimeTable(id);
                            alarmClass.deleteRepeatAlarm(Integer.parseInt(id) + 1000);
                            alarmClass.deleteRepeatAlarm(Integer.parseInt(id) + 10000);
                            finish();
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View buttonView) {


        switch (buttonView.getId()) {
            case R.id.sa_mCb:
                if (sa_mCb.isChecked()) {
                    repeat = MyAnnotations.ON;
                    isAllDaysCheckBoxChecked();
                    days = days + "," + sa;
                } else {
                    days = removeDays(days, sa);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.su_mCb:
                if (su_mCb.isChecked()) {
                    days = days + "," + su;
                    isAllDaysCheckBoxChecked();
                    repeat = MyAnnotations.ON;

                } else {
                    days = removeDays(days, su);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.mo_mCb:
                if (mo_mCb.isChecked()) {
                    isAllDaysCheckBoxChecked();
                    days = days + "," + mo;
                    repeat = MyAnnotations.ON;

                } else {
                    days = removeDays(days, mo);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.tu_mCb:
                if (tu_mCb.isChecked()) {
                    isAllDaysCheckBoxChecked();
                    days = days + "," + tu;
                    isAllDaysCheckBoxChecked();
                    repeat = MyAnnotations.ON;

                } else {
                    days = removeDays(days, tu);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.we_mCb:
                if (we_mCb.isChecked()) {
                    days = days + "," + we;
                    isAllDaysCheckBoxChecked();
                    repeat = MyAnnotations.ON;
                } else {
                    days = removeDays(days, we);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.th_mCb:
                if (th_mCb.isChecked()) {
                    days = days + "," + th;
                    isAllDaysCheckBoxChecked();
                    repeat = MyAnnotations.ON;

                } else {
                    days = removeDays(days, th);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.fr_mCb:
                if (fr_mCb.isChecked()) {
                    days = days + "," + fr;
                    isAllDaysCheckBoxChecked();
                    repeat = MyAnnotations.ON;

                } else {
                    days = removeDays(days, fr);
                    if (!isAllCheckBoxChecked()) {
                        repeat = MyAnnotations.OFF;
                    }
                }
                break;
            case R.id.allDays_mCb:
                if (allDays_mCb.isChecked()) {
                    days = sa + "," + su + "," + mo + "," + tu + "," + we + "," + th + "," + fr;
                    checkAllBox(true);
                    repeat = MyAnnotations.ON;

                } else {
                    checkAllBox(false);
                    repeat = MyAnnotations.OFF;
                    days = "";

                }
                break;
        }
        if (isOneCheckBoxChecked()) {
            if (isOutOfLimitEndDate() || isOutOfLimitStartDate()) {
                startTime = timeUtil.getFormattedTimeJust(System.currentTimeMillis());
                endTime = timeUtil.getFormattedTimeJust(System.currentTimeMillis() +
                        DateUtils.HOUR_IN_MILLIS);
                startDate_mtv.setText(startTime);
                endTime_mtv.setText(endTime);
            }
        } else {

            startDate = timeUtil.getFormattedDateAndTime(System.currentTimeMillis());
            endDate = timeUtil.getFormattedDateAndTime(System.currentTimeMillis() +
                    DateUtils.HOUR_IN_MILLIS);
            startDate_mtv.setText(startDate);
            endTime_mtv.setText(endDate);

        }
    }

    private void isAllDaysCheckBoxChecked() {
        if (isAllCheckBoxChecked()) {
            if (!allDays_mCb.isChecked()) {
                allDays_mCb.setChecked(true);
            }
        }
    }

    private void checkAllBox(boolean checkState) {
        sa_mCb.setChecked(checkState);
        su_mCb.setChecked(checkState);
        mo_mCb.setChecked(checkState);
        tu_mCb.setChecked(checkState);
        we_mCb.setChecked(checkState);
        th_mCb.setChecked(checkState);
        fr_mCb.setChecked(checkState);
    }

    public String removeDays(String string, String lookFor) {
        allDays_mCb.setChecked(false);
        StringBuilder outDays = new StringBuilder();
        String[] array = string.split(",");


        for (String item : array) {
            if (!item.matches(lookFor)) {
                outDays.append(",").append(item);
            }
        }
        outDays = new StringBuilder(outDays.toString().startsWith(",") ?
                outDays.substring(1) : outDays.toString());
        return outDays.toString();
    }

    public boolean isAllCheckBoxChecked() {
        return sa_mCb.isChecked() && su_mCb.isChecked() && mo_mCb.isChecked()
                && tu_mCb.isChecked() &&
                we_mCb.isChecked() &&
                th_mCb.isChecked() &&
                fr_mCb.isChecked();
    }

    public boolean isOneCheckBoxChecked() {
        return sa_mCb.isChecked() ||
                su_mCb.isChecked() ||
                mo_mCb.isChecked() ||
                tu_mCb.isChecked() ||
                we_mCb.isChecked() ||
                th_mCb.isChecked() ||
                fr_mCb.isChecked();
    }

    public boolean isOutOfLimitStartDate() {
        int startLength = startDate.length();
        return startLength > 11;
    }

    public boolean isOutOfLimitEndDate() {
        int startLength = startDate.length();
        return startLength > 11;
    }

    public boolean isOneFieldRemains() {
        return timeBaseProfiler_mEt.getText().length() <= 0 ||
                startDate_mtv.getText().length() <= 0 ||
                startProfile_mtv.getText().length() <= 0 ||
                endTime_mtv.getText().length() <= 0 ||
                endProfile_mtv.getText().length() <= 0;

    }

    /* public int lastItemId()
    {

    }*/
}
