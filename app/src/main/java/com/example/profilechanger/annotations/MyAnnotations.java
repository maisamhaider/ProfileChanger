package com.example.profilechanger.annotations;


import android.media.AudioManager;

import com.example.profilechanger.R;

public @interface MyAnnotations {

    String RINGER_MODE_SILENT_KEY = "RINGER_MODE_SILENT_KEY";
    String RINGER_MODE_SILENT = "RINGER_MODE_SILENT";
    String RINGER_MODE_VIBRATE = "RINGER_MODE_VIBRATE";
    String RINGER_MODE_NORMAL = "RINGER_MODE_NORMAL";
    String ADJUST_MUTE = "ADJUST_MUTE";
    String RINGING = "Ringing";
    String SILENT = "Silent";
    String VIBRATE = "Vibrate";
    String HELP_FIRST_TIME = "HELP_FIRST_TIME";
    String PROFILE_ID = "PROFILE_ID";
    String PROFILE_NEW = "PROFILE_NEW";
    String TIME_PROFILER_ID = "TIME_PROFILER_ID";
    String LOCATION_PROFILER_ID = "LOCATION_PROFILER_ID";
    String IS_UPDATE = "IS_UPDATE";

    String SA = "Sa";
    String SU = "Su";
    String MO = "Mo";
    String TU = "Tu";
    String WE = "We";
    String TH = "Th";
    String FR = "Fr";

    String ONE = "1";
    String ZERO = "0";

    int STREAM_MUSIC = AudioManager.STREAM_MUSIC;
    int STREAM_SYSTEM = AudioManager.STREAM_SYSTEM;
    int STREAM_NOTIFICATION = AudioManager.STREAM_NOTIFICATION;
    int STREAM_RING = AudioManager.STREAM_RING;

    String UN_DONE = "UN_DONE";
    String DONE = "DONE";
    String BOOT_COMPLETED = "BOOT_COMPLETED";
    String ON = "ON";
    String OFF = "OFF";
    String ENTER = "ENTER";
    String EXIT = "EXIT";
    String BOTH = "BOTH";
    String DO_NOT_DISTURB_MESSAGE = "If you want to use this feature you need allow the permission";
    String GO_WIFI_SETTINGS_MESSAGE = "You have to turn ON/OFF from settings. Thank you";
    String NULL = "NULL";

    String GEO_FENCE_LIMIT = "GEO_FENCE_LIMIT";

    String MY_PREFERENCES = "MY_PREFERENCES";

    String BY_WALK = "60x60 (m)";
    String BY_CYCLE = "100x100 (m)";
    String BY_BUS = "200x200 (m)";
    String BY_CAR = "250x250 (m)";

    String CHANNEL_ID = "CHANNEL_ID";
    String CHANNEL_NAME = R.string.app_name + "channel";

    String IS_TERMS_CONDITION = "isTermCondition";
    String wait_message = "Please wait a moment";


    String delete = "delete";
    String edit = "edit";
    String back = "back";
    String done = "done";
    String next = "next";
    String PRE_PROFILES_LOADED = "PRE_PROFILES_LOADED";
}
