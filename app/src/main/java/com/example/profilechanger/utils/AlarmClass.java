package com.example.profilechanger.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.broadcasts.AlarmReceiver;

public class AlarmClass extends ContextWrapper {


    public AlarmClass(Context base) {
        super(base);

    }

    public void setOneAlarm(String title, long reminderTime, int position,boolean isRepeat) {
        PendingIntent  pendingIntent=null;
        AlarmManager alarmManager=null;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(MyAnnotations.PROFILER_TITLE, title);
        myIntent.putExtra(MyAnnotations.PROFILER_POSITION, position);
         myIntent.putExtra(MyAnnotations.IS_REPEAT, isRepeat);
         myIntent.putExtra(MyAnnotations.TRIGGER_TIME, reminderTime);

        pendingIntent = PendingIntent.getBroadcast(this, position, myIntent,
                0);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
        }


    }
//    public void alarmEditFun(String title,long reminderTime,int position)
//    {
//        AlarmManager  alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );
//        Intent  myIntent = new Intent( this, NotificationReceiver.class );
//        myIntent.putExtra( "Task_Title",title );
//        myIntent.putExtra( "Position",position );
//        myIntent.putExtra( "Is_Repeating",false );
//
//        PendingIntent  pendingIntent = PendingIntent.getBroadcast( this,position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT );
//
//        if (alarmManager != null) {
//            alarmManager.cancel( pendingIntent );
//            alarmManager.setExact( AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent );
//        }
//    }

//    public void repeatingAlarm(String title, long reminderTime, int position, long intervalTime) {
//
//
//        Intent myIntent1 = new Intent(this, AlarmReceiver.class);
//        myIntent1.putExtra(MyAnnotations.PROFILER_TITLE, title);
//        myIntent1.putExtra(MyAnnotations.IS_REPEAT, true);
//        myIntent1.putExtra(MyAnnotations.PROFILER_POSITION, position);
//        myIntent1.putExtra(MyAnnotations.TRIGGER_TIME, reminderTime);
//
//
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, position,
//                myIntent1, 0);
//        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        if (alarmManager1 != null) {
//            alarmManager1.cancel(pendingIntent1);
//            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, reminderTime,
//                    AlarmManager.INTERVAL_DAY, pendingIntent1);
//        }
//    }

    public void deleteRepeatAlarm(int position) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), position,
                intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


}
