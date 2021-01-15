package com.example.profilechanger.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.app.Service;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.profilechanger.R;


public class Service1 extends Service {
     private Context context;


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

}