package com.example.profilechanger.broadcasts;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.sharedpreferences.MyPreferences;
import com.example.profilechanger.utils.AlarmClass;
import com.example.profilechanger.utils.TimeUtil;


public class BootCompletedReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        MyPreferences preferences = new MyPreferences(context);
        preferences.setBoolean(MyAnnotations.BOOT_COMPLETED, true);
        registerAlarms(context);
    }

    public void registerAlarms(Context context) {
        MyDatabase database = new MyDatabase(context);
        AlarmClass alarmClass = new AlarmClass(context);
        TimeUtil timeUtil = new TimeUtil(context);
        Cursor cursor = database.retrieveTimeTable();

        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            String profilerTitle = cursor.getString(1);
            String startDate = cursor.getString(4);
            String endDate = cursor.getString(5);
            String state = cursor.getString(6);
            String repeat = cursor.getString(8);


            if (state.matches(MyAnnotations.UN_DONE)) {
                if (repeat.matches(MyAnnotations.OFF)) {
                    //set start Alarm
                    long triggerTime1 = timeUtil.getMillisFromFormattedDate(startDate,
                            MyAnnotations.DEFAULT_FORMAT);

                    alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                            Integer.parseInt(id) + 1000,false);
                    //set end Alarm
                    long triggerTime2 = timeUtil.getMillisFromFormattedDate(endDate,
                            MyAnnotations.DEFAULT_FORMAT);

                    alarmClass.setOneAlarm(profilerTitle, triggerTime2,
                            Integer.parseInt(id) + 10000,false);

                } else {
                    long triggerTime1 = timeUtil.getMillisFromFormattedDate(startDate,
                            MyAnnotations.DEFAULT_TIME_FORMAT);
                    alarmClass.setOneAlarm(profilerTitle, triggerTime1,
                            Integer.parseInt(id) + 1000,true);



                }
            }
        }

    }
}
