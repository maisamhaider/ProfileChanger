package com.example.profilechanger.broadcasts;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.database.MyDatabase;
import com.example.profilechanger.notification.NotificationHelper;
import com.example.profilechanger.utils.SoundProfileActions;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class GeoFenceReceiver extends BroadcastReceiver {
    private static String TAG = "GeoFenceReceiver";
    NotificationHelper notificationHelper;
    private Context context;
    SoundProfileActions soundProfileActions;
    MyDatabase db;
    NotificationManager mNotificationManager;
    SeekBar seekBar;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        notificationHelper = new NotificationHelper(context);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        seekBar = new SeekBar(context);
        if (geofencingEvent.hasError()) {
            return;
        }


        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.i(TAG, geofence.getRequestId());
        }
        soundProfileActions = new SoundProfileActions(context);
        db = new MyDatabase(context);
        Cursor cursor = db.retrieveLocation(geofenceList.get(0).getRequestId());
        String type = "null";
        while (cursor.moveToNext()) {
            type = cursor.getString(6);
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                if (type.matches(MyAnnotations.ENTER)) {
                    enter(false, geofenceList.get(0).getRequestId());

                }
                if (type.matches(MyAnnotations.BOTH)) {
                    enter(true, geofenceList.get(0).getRequestId());
                }
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                exit(geofenceList.get(0).
                        getRequestId());
                break;
        }

    }


    public void removeGeoFence(String id) {
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
        List<String> idList = new ArrayList<>();
        idList.add(String.valueOf(id));
        geofencingClient.removeGeofences(idList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void audio(String ringingMode, SoundProfileActions soundProfileActions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                soundProfileActions.setRingerMode(ringingMode);
            }


        } else {
            soundProfileActions.setRingerMode(ringingMode);
        }
    }

    public void setVibrate(int zeroOne) {
        soundProfileActions.setVibration(zeroOne);
    }

    public void setTouchSound(int zeroOne) {
        soundProfileActions.setTouchSound(zeroOne);
    }

    public void setDialPadSound(int zeroOne) {
        soundProfileActions.setDialingPadTone(zeroOne);
    }

    public void setVolume(int type, int level) {
        soundProfileActions.setVolume(type, level);
    }

    public void enter(boolean isBoth, String id) {
        Cursor cursor = db.retrieveLocation(id);
        if (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String state = cursor.getString(8);
            String date = cursor.getString(9);
            String enterProfileId = cursor.getString(10);
            String endProfileId = cursor.getString(11);


            String profileId;
            String PROFILE_TITLE;
            String ringerMode = MyAnnotations.RINGER_MODE_NORMAL;
            int ringerLevel = 0;
            int mediaLevel = 0;
            int notiLevel = 0;
            int systemLevel = 0;
            String vibrate = MyAnnotations.OFF;
            String touchSound = MyAnnotations.OFF;
            String dialPadSound = MyAnnotations.OFF;

            //get profile data
            Cursor cursor1 = db.retrieveLocation(enterProfileId);
            if (cursor1.moveToNext()) {
                ringerMode = cursor1.getString(2);
                ringerLevel = Integer.parseInt(cursor1.getString(3));
                mediaLevel = Integer.parseInt(cursor1.getString(4));
                notiLevel = Integer.parseInt(cursor1.getString(5));
                systemLevel = Integer.parseInt(cursor1.getString(6));
                vibrate = cursor1.getString(7);
                touchSound = cursor1.getString(8);
                dialPadSound = cursor1.getString(9);
            }

            //set actions
            audio(ringerMode, soundProfileActions);
            if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                setVibrate(0);
            } else {
                if (vibrate.matches(MyAnnotations.ON)) {
                    setVibrate(1);
                } else {
                    setVibrate(0);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                    setVolume(AudioManager.STREAM_RING, ringerLevel);
                    setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                    setVolume(AudioManager.STREAM_NOTIFICATION, notiLevel);
                    setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                }


            } else {
                setVolume(AudioManager.STREAM_RING, ringerLevel);
                setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                setVolume(AudioManager.STREAM_NOTIFICATION, notiLevel);
                setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
            }


            if (touchSound.matches(MyAnnotations.ON)) {
                setTouchSound(1);

            } else {
                setTouchSound(0);

            }
            if (dialPadSound.matches(MyAnnotations.ON)) {
                setDialPadSound(1);

            } else {
                setDialPadSound(0);

            }

            if (!isBoth) {
                removeGeoFence(id);
                db.update(id, MyAnnotations.DONE);
            }

            notificationHelper.sendHighPriorityNotification(title, "is Triggered on enter");


        }


    }

    public void exit(String id) {

        Cursor cursor = db.retrieveLocation(id);
        if (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String endProfileId = cursor.getString(11);

            String ringerMode = MyAnnotations.RINGER_MODE_NORMAL;
            int ringerLevel = 0;
            int mediaLevel = 0;
            int notiLevel = 0;
            int systemLevel = 0;
            String vibrate = MyAnnotations.OFF;
            String touchSound = MyAnnotations.OFF;
            String dialPadSound = MyAnnotations.OFF;

            //get profile data
            Cursor cursor1 = db.retrieveLocation(endProfileId);
            if (cursor1.moveToNext()) {
                ringerMode = cursor1.getString(2);
                ringerLevel = Integer.parseInt(cursor1.getString(3));
                mediaLevel = Integer.parseInt(cursor1.getString(4));
                notiLevel = Integer.parseInt(cursor1.getString(5));
                systemLevel = Integer.parseInt(cursor1.getString(6));
                vibrate = cursor1.getString(7);
                touchSound = cursor1.getString(8);
                dialPadSound = cursor1.getString(9);
            }

            //set actions
            if (ringerMode.matches(MyAnnotations.RINGER_MODE_SILENT)) {
                setVibrate(0);
            } else {
                if (vibrate.matches(MyAnnotations.ON)) {
                    setVibrate(1);
                } else {
                    setVibrate(0);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                    setVolume(AudioManager.STREAM_RING, ringerLevel);
                    setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                    setVolume(AudioManager.STREAM_NOTIFICATION, notiLevel);
                    setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
                    audio(ringerMode, soundProfileActions);
                }

            } else {
                setVolume(AudioManager.STREAM_RING, ringerLevel);
                setVolume(AudioManager.STREAM_MUSIC, mediaLevel);
                setVolume(AudioManager.STREAM_NOTIFICATION, notiLevel);
                setVolume(AudioManager.STREAM_SYSTEM, systemLevel);
            }

            if (touchSound.matches(MyAnnotations.ON)) {
                setTouchSound(1);

            } else {
                setTouchSound(0);

            }
            if (dialPadSound.matches(MyAnnotations.ON)) {
                setDialPadSound(1);

            } else {
                setDialPadSound(0);

            }

            removeGeoFence(id);
            db.update(id, MyAnnotations.DONE);
            notificationHelper.sendHighPriorityNotification(title, "is Triggered on exit");


        }
    }


}
