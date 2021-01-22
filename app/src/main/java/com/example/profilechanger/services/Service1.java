package com.example.profilechanger.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.notification.NotificationHelper;
import com.example.profilechanger.utils.SoundProfileActions;
import com.example.profilechanger.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;


public class Service1 extends Service {

    Context context;
    SoundProfileActions actions;
    MyDatabase db;
    NotificationHelper notificationHelper;
    NotificationManager mNotificationManager;
    TimeUtil timeUtil;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "channel_id_1";
        String channelName = "Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_LOW);
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        actions = new SoundProfileActions(this);
        timeUtil = new TimeUtil(this);
        db = new MyDatabase(this);
        notificationHelper = new NotificationHelper(this);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                    createNotificationChannel(notificationManager) : getString(R.string.app_name);
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this, channelId).
                    setSmallIcon(R.drawable.ic_main)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Apps service run to perform function correctly.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Apps service run to perform function correctly"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_main)
                    .setContentInfo(" ")
                    .setCategory(NotificationCompat.CATEGORY_SERVICE).build();
            startForeground(100, notification);
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e("Service1","hhj");
                startProfile();
            }
        }, 1_000, 40_000);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android
        AlarmManager alarmService = (AlarmManager)
                getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 100, restartServicePI);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public void startProfile() {
        actions = new SoundProfileActions(context);
        Cursor cursor = db.retrieveTimeTable();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String start_time = cursor.getString(4);
            String end_time = cursor.getString(5);
            String state = cursor.getString(6);
            String repeat = cursor.getString(8);
            String time_profile_start_id = cursor.getString(10);
            String time_profile_end_id = cursor.getString(11);
            String sState = cursor.getString(12);
            String eState = cursor.getString(13);
            String TIME_S_SUNDAY = cursor.getString(14);
            String TIME_S_MONDAY = cursor.getString(15);
            String TIME_S_TUESDAY = cursor.getString(16);
            String TIME_S_WEDNESDAY = cursor.getString(17);
            String TIME_S_THURSDAY = cursor.getString(17);
            String TIME_S_FRIDAY = cursor.getString(18);
            String TIME_S_SATURDAY = cursor.getString(19);
            String TIME_E_SUNDAY = cursor.getString(20);
            String TIME_E_MONDAY = cursor.getString(21);
            String TIME_E_TUESDAY = cursor.getString(22);
            String TIME_E_WEDNESDAY = cursor.getString(23);
            String TIME_E_THURSDAY = cursor.getString(24);
            String TIME_E_FRIDAY = cursor.getString(25);
            String TIME_E_SATURDAY = cursor.getString(26);

            String toDay = timeUtil.getToday();
            long triggerTimeStart;
            long triggerTimeEnd;
            if (repeat.equals(MyAnnotations.ON)) {
                long currentTime = timeUtil.getCurrentTimeJust();
                triggerTimeStart = timeUtil.getMillisFromFormattedDate(start_time,
                        MyAnnotations.DEFAULT_TIME_FORMAT);
                triggerTimeEnd = timeUtil.getMillisFromFormattedDate(end_time,
                        MyAnnotations.DEFAULT_TIME_FORMAT);
                if (isToday(id)) {
                    //sunday
                    if (toDay.equals(MyAnnotations.SUN)) {
                        if (TIME_S_SUNDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);
                            }
                        }
                        if (TIME_E_SUNDAY.equals(MyAnnotations.UN_DONE)) {

                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.MON)) {
                        if (TIME_S_MONDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_MONDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);

                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.TUE)) {
                        if (TIME_S_TUESDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_TUESDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.WED)) {
                        if (TIME_S_WEDNESDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_WEDNESDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.THU)) {
                        if (TIME_S_THURSDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.DONE, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_THURSDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.DONE, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.FRI)) {
                        if (TIME_S_FRIDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {

                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.DONE, MyAnnotations.UN_DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_FRIDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.DONE,
                                        MyAnnotations.UN_DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }

                    } else if (toDay.equals(MyAnnotations.SAT)) {
                        if (TIME_S_SATURDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeStart) {
                                db.updateStartStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.DONE);
                                sounds(time_profile_start_id, title);

                            }
                        }
                        if (TIME_E_SATURDAY.equals(MyAnnotations.UN_DONE)) {
                            if (currentTime >= triggerTimeEnd) {
                                db.updateEndStates(id, MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE
                                        , MyAnnotations.UN_DONE, MyAnnotations.UN_DONE,
                                        MyAnnotations.DONE);
                                sounds(time_profile_end_id, title);
                            }
                        }
                    }
                }
            } else {
                triggerTimeStart = timeUtil.getMillisFromFormattedDate(start_time,
                        MyAnnotations.DEFAULT_FORMAT);
                triggerTimeEnd = timeUtil.getMillisFromFormattedDate(end_time,
                        MyAnnotations.DEFAULT_FORMAT);
                if (state.equals(MyAnnotations.UN_DONE)) {
                    long currentTime = timeUtil.getCurrentTime(MyAnnotations.DEFAULT_FORMAT);
                    if (sState.equals(MyAnnotations.UN_DONE)) {
                        if (currentTime >= triggerTimeStart) {
                            sounds(time_profile_start_id, title);
                            db.updateStartState(id, MyAnnotations.DONE);
                        }
                    }
                    if (eState.equals(MyAnnotations.UN_DONE)) {
                        long currentTimeEnd = timeUtil.getCurrentTime(MyAnnotations.DEFAULT_FORMAT);
                        if (currentTimeEnd >= triggerTimeEnd) {
                            sounds(time_profile_end_id, title);
                            db.update(id, MyAnnotations.DONE);
                            db.updateEndState(id, MyAnnotations.DONE);
                        }
                    }
                }
            }
        }
    }

    public void sounds(String id, String title) {
        Cursor startCursor = db.retrieveProfile(id);
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
        MyDatabase database = new MyDatabase(context);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            String days = cursor.getString(9);
            if (days.contains(timeUtil.getToday())) {
                return true;
            }
        }
        return false;

    }

    public boolean isTomorrowContain(String id) {
        MyDatabase database = new MyDatabase(context);
        Cursor cursor = database.retrieveTimeTable(id);
        while (cursor.moveToNext()) {
            String days = cursor.getString(9);
            if (days.contains(new TimeUtil(context).getTomorrowDay())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service1","onDestroy hhj");

    }
}