package com.example.profilechanger.broadcasts;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

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

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationHelper = new NotificationHelper(context);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }


        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.i(TAG, geofence.getRequestId());
        }
        SoundProfileActions soundProfileActions = new SoundProfileActions(context);
        MyDatabase db = new MyDatabase(context);
        Cursor cursor = db.retrieveLocation(geofenceList.get(0).getRequestId());
        String title = "null";
        String type = "null";
        while (cursor.moveToNext()) {
            title = cursor.getString(1);
            type = cursor.getString(6);
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                if (type.matches(MyAnnotations.ENTER)) {
                    enter(false, context, soundProfileActions, db
                            , geofenceList.get(0).
                                    getRequestId(),
                            title);

                }
                if (type.matches(MyAnnotations.BOTH)) {
                    enter(true, context, soundProfileActions, db
                            , geofenceList.get(0).getRequestId(),
                            title);
                }
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                exit(context, soundProfileActions, db, geofenceList.get(0).
                        getRequestId(), title);
                break;
        }

    }


    public void removeGeoFence(Context context, String id) {
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

    public void audio(String ringingMode, Context context, SoundProfileActions soundProfileActions) {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                soundProfileActions.setRingerMode(ringingMode);
            }


        } else {
            soundProfileActions.setRingerMode(ringingMode);
        }
    }

    public void enter(boolean isBoth, Context context, SoundProfileActions soundProfileActions,
                      MyDatabase db, String id, String title) {

        if (isBoth) {
            notificationHelper.sendHighPriorityNotification(title, "you are entered to "
                    + title + " area");

        } else
            notificationHelper.sendHighPriorityNotification(title, "you are entered to "
                    + title + " area");
     }

    public void exit(Context context, SoundProfileActions soundProfileActions,
                     MyDatabase db, String id, String title) {

        notificationHelper.sendHighPriorityNotification(title, "you are exited from "
                + title + " area");

    }


}
