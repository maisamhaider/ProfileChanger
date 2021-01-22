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
    SoundProfileActions actions;
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
    /*    for (Geofence geofence : geofenceList) {
            Log.i(TAG, geofence.getRequestId());
        }*/
        actions = new SoundProfileActions(context);
        db = new MyDatabase(context);
        Cursor cursor = db.retrieveLocation(geofenceList.get(0).getRequestId());
        String type = "null";
        while (cursor.moveToNext()) {
            type = cursor.getString(5);
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
        actions.setVibration(zeroOne);
    }

    public void setTouchSound(int zeroOne) {
        actions.setTouchSound(zeroOne);
    }

    public void setDialPadSound(int zeroOne) {
        actions.setDialingPadTone(zeroOne);
    }

    public void setVolume(int type, int level) {
        actions.setVolume(type, level);
    }

    public void enter(boolean isBoth, String id) {
        Cursor cursor = db.retrieveLocation(id);
        if (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String enterProfileId = cursor.getString(10);

            Cursor startCursor = db.retrieveProfile(enterProfileId);

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

            if (!isBoth) {
                removeGeoFence(id);
                db.update(id, MyAnnotations.DONE);
            }



        }


    }

    public void exit(String id) {

        Cursor cursor = db.retrieveLocation(id);
        if (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String endProfileId = cursor.getString(11);

            //get profile data
            Cursor startCursor = db.retrieveProfile(endProfileId);
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

            removeGeoFence(id);
            db.update(id, MyAnnotations.DONE);

        }
    }


}
