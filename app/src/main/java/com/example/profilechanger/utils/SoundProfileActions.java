package com.example.profilechanger.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.profilechanger.annotations.MyAnnotations;


public class SoundProfileActions {
    private Context context;
    private AudioManager am;

    public SoundProfileActions(Context context) {
        this.context = context;
        am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

    }

    public SoundProfileActions(Context context, Activity activity) {
        this.context = context;
    }

//
//    public void setRingerMode(String whatToSet) {
//
//        am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//        //For Normal mode
//        if (whatToSet.matches(MyAnnotations.RINGER_MODE_NORMAL)) {
//
//            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//
//        } else
//            //For Silent mode
//            if (whatToSet.matches(MyAnnotations.RINGER_MODE_SILENT)) {
//
////             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                    am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI);
////                } else {
//                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
////                }
//            } else
//                //For Vibrate mode
//                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//
//    }

    public void setRingerMode(String whatToSet) {
        //For Normal mode
        if (whatToSet.matches(MyAnnotations.RINGER_MODE_NORMAL)) {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        } else
            //For Silent mode
            if (whatToSet.matches(MyAnnotations.RINGER_MODE_SILENT)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.adjustVolume(AudioManager.ADJUST_MUTE, 0);

                }
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else
                //For Vibrate mode
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

    }

    public void setVolume( int stream, int volume) {

        am.setStreamVolume(stream, (int) ((int)volume*(0.15f)), AudioManager.FLAG_SHOW_UI);

    }

    public boolean getDialingPadTone() {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.DTMF_TONE_WHEN_DIALING, 1) != 0;
    }

    public boolean getTouchSound() {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.SOUND_EFFECTS_ENABLED, 1) != 0;

    }


    public boolean getVibration() {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.VIBRATE_ON, 1) != 0;
    }

    public void setDialingPadTone(int onOff) {

        Settings.System.putInt(context.getContentResolver(),
                Settings.System.DTMF_TONE_WHEN_DIALING, onOff);
    }

    public void setTouchSound(int onOff) {

        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SOUND_EFFECTS_ENABLED, onOff);

    }


    public void setVibration(int onOff) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.VIBRATE_ON, onOff);
    }


    public String getRingerMode() {
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                return MyAnnotations.RINGER_MODE_SILENT;
            case AudioManager.RINGER_MODE_VIBRATE:
                return MyAnnotations.RINGER_MODE_VIBRATE;
            case AudioManager.RINGER_MODE_NORMAL:
                return MyAnnotations.RINGER_MODE_NORMAL;
            case AudioManager.ADJUST_MUTE:
                return MyAnnotations.ADJUST_MUTE;
        }
        return "null";

    }

}
