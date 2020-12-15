package com.example.profilechanger.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.profilechanger.annotations.MyAnnotations;
import com.example.profilechanger.sharedpreferences.MyPreferences;


public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyPreferences preferences = new MyPreferences(context);
        preferences.setBoolean(MyAnnotations.BOOT_COMPLETED,true);
    }
}
