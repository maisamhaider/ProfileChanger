package com.example.profilechanger.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;

import java.util.Random;


public class NotificationHelper extends ContextWrapper {
    Context context;
    private final boolean isVibrate;
    private final Uri soundUri;

    public NotificationHelper(Context base) {
        super(base);
        this.context = base;
        MyPreferences preferences = new MyPreferences(this);
        NotificationSounds notificationSounds = new NotificationSounds(context);
        isVibrate = preferences.getBoolean(MyAnnotations.NOTIFICATION_VIBRATE,
                false);
        soundUri = Uri.parse(preferences.getString(MyAnnotations.NOTIFICATION_SOUND_PATH,
                notificationSounds.getNotificationSoundsPath().get(1)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(MyAnnotations.CHANNEL_ID,
                MyAnnotations.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(isVibrate);
        notificationChannel.setSound(soundUri,null);
        notificationChannel.setDescription("Chanel for " + context.getResources().getString(
                R.string.app_name));
        notificationChannel.setLightColor(getResources().getColor(R.color.black));
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    public void sendHighPriorityNotification(String title, String body/*, Class activityName*/) {

//        Intent intent = new Intent(this, activityName);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,
                MyAnnotations.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_main)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()/*.setSummaryText("summary")*/
                        .setBigContentTitle(title).bigText(body))
                .setAutoCancel(true)
                .build();

//                .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
    }

}
