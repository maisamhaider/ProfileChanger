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


    String SAT = "Sat";
    String SUN = "Sun";
    String MON = "Mon";
    String TUS = "Tue";
    String WED = "Wed";
    String THU = "Thu";
    String FRI = "Fri";

    String START_PROFILE_ID = "START_PROFILE_ID";
    String END_PROFILE_ID = "END_PROFILE_ID";

    String DEFAULT_DATE_FORMAT = "dd:MM:yyyy";
    String DEFAULT_TIME_FORMAT = "h:mm a";
    String DEFAULT_FORMAT = DEFAULT_DATE_FORMAT +" "+ DEFAULT_TIME_FORMAT;

    String ONE = "1";
    String ZERO = "0";



    String UN_DONE = "UN_DONE";
    String DONE = "DONE";
    String BOOT_COMPLETED = "BOOT_COMPLETED";
    String ON = "ON";
    String OFF = "OFF";
    String ENTER = "ENTER";
    String NEVER = "Never";
    String EXIT = "EXIT";
    String BOTH = "BOTH";
    String DO_NOT_DISTURB_MESSAGE = "If you want to use this feature you need allow the permission";
    String GO_WIFI_SETTINGS_MESSAGE = "You have to turn ON/OFF from settings. Thank you";
    String NULL = "NULL";

    String GEO_FENCE_LIMIT = "GEO_FENCE_LIMIT";

    String MY_PREFERENCES = "MY_PREFERENCES";

    String BY_WALK = "100x100 (m)";
    String BY_CYCLE = "150x150 (m)";
    String BY_BUS = "250x250 (m)";
    String BY_CAR = "300x300 (m)";

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
    String PROFILER_TITLE = "PROFILER_TITLE";
    String PROFILER_POSITION = "PROFILER_POSITION";
    String IS_REPEAT = "IS_REPEAT";
    String TRIGGER_TIME = "TRIGGER_TIME";
    String NOTIFICATION_SOUND_NAME = "NOTIFICATION_SOUND_NAME";
    String NOTIFICATION_SOUND_PATH = "NOTIFICATION_SOUND_PATH";
    String NOTIFICATION_VIBRATE = "NOTIFICATION_VIBRATE";

    String THEME = "THEME";
    String DAY = "DAY";
    String NIGHT = "NIGHT";
    String AUTO_CHANGE = "AUTO_CHANGE";
    String N_A = "N_A";
    String IS_LIGHT_THEME = "IS_LIGHT_THEME";
    String IS_APP_FIRST_TIME = "IS_APP_FIRST_TIME";
}
