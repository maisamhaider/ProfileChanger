package com.example.profilechanger.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;

import java.util.Random;


public class NotificationHelper extends ContextWrapper {
    Context context;

    public NotificationHelper(Context base) {
        super(base);
        this.context = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(MyAnnotations.CHANNEL_ID,
                MyAnnotations.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
         notificationChannel.setDescription("profile is triggered");
        notificationChannel.setLightColor(getResources().getColor(R.color.black));
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    public void sendHighPriorityNotification(String title, String body/*, Class activityName*/) {

//        Intent intent = new Intent(this, activityName);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,
                MyAnnotations.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()/*.setSummaryText("summary")*/
                        .setBigContentTitle(title).bigText(body))
                .setAutoCancel(true)
                .build();

//                .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
    }

}
