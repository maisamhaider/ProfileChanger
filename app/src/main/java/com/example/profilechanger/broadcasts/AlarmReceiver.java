package com.example.profilechanger.broadcasts;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.notification.NotificationHelper;
import com.example.profilechanger.utils.SoundProfileActions;
import com.example.profilechanger.utils.TimeUtil;


public class AlarmReceiver extends BroadcastReceiver {


    Context mContext;
    private NotificationHelper notificationHelper;
    private SoundProfileActions actions;

    private String time_profile_start_id;
    private String time_profile_end_id;
    String title;
    private TimeUtil timeUtil;
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        notificationHelper = new NotificationHelper(context);
        timeUtil = new TimeUtil(context);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        title = intent.getStringExtra(MyAnnotations.PROFILER_TITLE);
        int id = intent.getIntExtra(MyAnnotations.PROFILER_POSITION, 0);
        boolean repeat = intent.getBooleanExtra(MyAnnotations.IS_REPEAT, false);
        long triggerTime = intent.getLongExtra(MyAnnotations.TRIGGER_TIME, 0);
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;

        if (!manufacturer.toLowerCase().matches("huawei")
                && !model.toLowerCase().matches("stk-l21")) {
        if (id - 10000 > -1) {
            //mean this is end profile
            if (repeat) {
                if (isToday(String.valueOf(id - 10000))) {
                    endProfile(String.valueOf(id - 10000));
                }

            } else {
                endProfile(String.valueOf(id - 10000));
            }
        } else {
            //mean this is start profile
            if (repeat) {
                if (isToday(String.valueOf(id - 1000))) {
                    startProfile(String.valueOf(id - 1000));
                }
            } else {
                startProfile(String.valueOf(id - 1000));
            }
        }

        }

    }

    public void startProfile(String id) {
        MyDatabase database = new MyDatabase(mContext);
        actions = new SoundProfileActions(mContext);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            time_profile_start_id = cursor.getString(10);
//            String isRepeat = cursor.getString(8);
//            AlarmClass alarmClass = new AlarmClass(mContext);
//            String endTime = cursor.getString(5);
//            long triggerTime;
//            if (isRepeat.matches(MyAnnotations.ON)) {
//                triggerTime = timeUtil.getMillisFromFormattedDate(
//                        timeUtil.getCurrentFormattedDate() + " " + endTime,
//                        MyAnnotations.DEFAULT_FORMAT);
//
//                alarmClass.setOneAlarm(title, triggerTime,
//                        Integer.parseInt(id) + 10000, true);
//            } else {
//                triggerTime = timeUtil.getMillisFromFormattedDate(endTime,
//                        MyAnnotations.DEFAULT_FORMAT);
//                alarmClass.setOneAlarm(title, triggerTime,
//                        Integer.parseInt(id) + 10000, false);
//            }
//            String d = timeUtil.getFormattedDateAndTime(triggerTime);
        }


        Cursor startCursor = database.retrieveProfile(time_profile_start_id);
        while (startCursor.moveToNext()) {
            String PROFILE_TITLE = startCursor.getString(1);
            String ringerMode = startCursor.getString(2);
            int ringerLevel = Integer.parseInt(startCursor.getString(3));
            int mediaLevel = Integer.parseInt(startCursor.getString(4));
            int notificationLevel = Integer.parseInt(startCursor.getString(5));
            int systemLevel = Integer.parseInt(startCursor.getString(6));
            String vibrate = startCursor.getString(7);
            String touchSound = startCursor.getString(8);
            String dialPedSound = startCursor.getString(9);
            notificationHelper.sendHighPriorityNotification(title, "Profile is triggered."
                    + PROFILE_TITLE + " is enabled");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {

                    if (ringerMode.equals(MyAnnotations.RINGER_MODE_SILENT)) {
                        actions.setRingerMode(ringerMode);
                        actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                    } else {
                        actions.setVolume(AudioManager.STREAM_RING, ringerLevel);
                        actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                        actions.setVolume(AudioManager.STREAM_NOTIFICATION, notificationLevel);
                        actions.setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                    }
                }

            } else {

                if (ringerMode.equals(MyAnnotations.RINGER_MODE_SILENT)) {
                    actions.setRingerMode(ringerMode);
                    actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);

                } else {
                    actions.setVolume(AudioManager.STREAM_RING, ringerLevel);
                    actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                    actions.setVolume(AudioManager.STREAM_NOTIFICATION, notificationLevel);
                    actions.setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                }
            }
            if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                actions.setVibration(0);

            } else {
                if (vibrate.matches(MyAnnotations.ON)) {
                    actions.setVibration(1);
                } else {
                    actions.setVibration(0);
                }
            }

            if (touchSound.matches(MyAnnotations.ON)) {
                actions.setTouchSound(1);
            } else {
                actions.setTouchSound(0);
            }
            if (dialPedSound.matches(MyAnnotations.ON)) {
                actions.setDialingPadTone(1);
            } else {
                actions.setDialingPadTone(0);
            }
        }


    }

    public void endProfile(String id) {
        MyDatabase database = new MyDatabase(mContext);
        actions = new SoundProfileActions(mContext);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            time_profile_end_id = cursor.getString(11);

            String isRepeat = cursor.getString(8);

            /*if (isRepeat.matches(MyAnnotations.ON)) {
                if (isTomorrowContain(id)) {
                    String startTime = cursor.getString(5);
                    //set start Alarm
                    long triggerTime = timeUtil.getMillisFromFormattedDate(
                            timeUtil.getCurrentFormattedDate() + " "
                                    + startTime,
                            MyAnnotations.DEFAULT_FORMAT);

                    long triggerTime1 =
                            timeUtil.getMilliDateAndTime(triggerTime +
                                    DateUtils.HOUR_IN_MILLIS * 24);

                    AlarmClass alarmClass = new AlarmClass(mContext);

                    String wq = timeUtil.getFormattedDateAndTime(triggerTime1);
                    alarmClass.setOneAlarm(title, triggerTime1,
                            Integer.parseInt(id) + 1000, true);
                }
            }*/

        }

        Cursor startCursor = database.retrieveProfile(time_profile_end_id);
        while (startCursor.moveToNext()) {
            String PROFILE_TITLE = startCursor.getString(1);
            String ringerMode = startCursor.getString(2);
            int ringerLevel = Integer.parseInt(startCursor.getString(3));
            int mediaLevel = Integer.parseInt(startCursor.getString(4));
            int notificationLevel = Integer.parseInt(startCursor.getString(5));
            int systemLevel = Integer.parseInt(startCursor.getString(6));
            String vibrate = startCursor.getString(7);
            String touchSound = startCursor.getString(8);
            String dialPedSound = startCursor.getString(9);
            notificationHelper.sendHighPriorityNotification(title, "Profile is triggered."
                    + PROFILE_TITLE + " is enabled");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {

                    if (ringerMode.equals(MyAnnotations.RINGER_MODE_SILENT)) {
                        actions.setRingerMode(ringerMode);
                        actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);

                    } else {
                        actions.setVolume(AudioManager.STREAM_RING, ringerLevel);
                        actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                        actions.setVolume(AudioManager.STREAM_NOTIFICATION, notificationLevel);
                        actions.setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                    }
                }

            } else {

                if (ringerMode.equals(MyAnnotations.RINGER_MODE_SILENT)) {
                    actions.setRingerMode(ringerMode);
                    actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                } else {
                    actions.setVolume(AudioManager.STREAM_RING, ringerLevel);
                    actions.setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                    actions.setVolume(AudioManager.STREAM_NOTIFICATION, notificationLevel);
                    actions.setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                }
            }

            if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                actions.setVibration(0);

            } else {
                if (vibrate.matches(MyAnnotations.ON)) {
                    actions.setVibration(1);
                } else {
                    actions.setVibration(0);
                }
            }

            if (touchSound.matches(MyAnnotations.ON)) {
                actions.setTouchSound(1);
            } else {
                actions.setTouchSound(0);
            }

            if (dialPedSound.matches(MyAnnotations.ON)) {
                actions.setDialingPadTone(1);
            } else {
                actions.setDialingPadTone(0);
            }


        }

    }

    public boolean isToday(String id) {
        MyDatabase database = new MyDatabase(mContext);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            String days = cursor.getString(9);
            if (days.contains(new TimeUtil(mContext).getToday())) {
                return true;
            }
        }
        return false;

    }

    public boolean isTomorrowContain(String id) {
        MyDatabase database = new MyDatabase(mContext);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            String days = cursor.getString(9);
            if (days.contains(new TimeUtil(mContext).getTomorrowDay())) {
                return true;
            }
        }
        return false;

    }


}
