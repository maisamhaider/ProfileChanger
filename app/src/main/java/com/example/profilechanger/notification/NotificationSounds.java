package com.example.profilechanger.notification;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.media.RingtoneManager;

import java.util.ArrayList;

public class NotificationSounds extends ContextWrapper {
    public NotificationSounds(Context base) {
        super( base );
    }

    public ArrayList<String> getNotificationSoundsName() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = manager.getCursor();

        ArrayList<String> listOFNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            String title = cursor.getString( RingtoneManager.TITLE_COLUMN_INDEX );
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            String combinedString = uri + "/" + id+"/"+title;
            String finalSoundName = combinedString.substring( combinedString.lastIndexOf( "/" )+1 );
            listOFNames.add(finalSoundName);
        }

        return listOFNames;
    }
    public ArrayList<String> getNotificationSoundsPath()
    {

        RingtoneManager ringtoneManager = new RingtoneManager( this );
        ringtoneManager.setType( RingtoneManager.TYPE_NOTIFICATION );
        Cursor cursor = ringtoneManager.getCursor();

        ArrayList<String> listOfPaths = new ArrayList<>(  );
        while (cursor.moveToNext())
        {
            String id = cursor.getString( RingtoneManager.ID_COLUMN_INDEX );
            String uri = cursor.getString( RingtoneManager.URI_COLUMN_INDEX );

            listOfPaths.add( uri +"/"+id );
        }
        return listOfPaths;
    }
}
