package com.example.profilechanger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.profilechanger.annotations.MyAnnotations;


public class MyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GEO_FENCE_DB";

    private static final String LOCATION_TABLE = "LOCATION_TABLE";
    private static final String LOCATION_ID = "LOCATION_ID";
    private static final String LOCATION_TITLE = "LOCATION_TITLE";
     private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String RADIUS = "RADIUS";
    private static final String GEO_FENCE_TYPE = "GEO_FENCE_TYPE";
    private static final String EXPIRATION_TIME = "EXPIRATION_TIME";
    private static final String LOCATION_STATE = "LOCATION_STATE";
    private static final String LOCATION_DATE = "LOCATION_DATE";
    private static final String LOCATION_PROFILE_ENTER = "LOCATION_PROFILE_ENTER";
    private static final String LOCATION_PROFILE_EXIT = "LOCATION_PROFILE_EXIT";


    private static final String TIME_TABLE = "TIME_TABLE";
    private static final String TIME_ID = "TIME_ID";
    private static final String TIME_TITLE = "TIME_TITLE";
    private static final String TIME_PROFILE_TITLE_START = "TIME_PROFILE_TITLE_START";
    private static final String TIME_PROFILE_TITLE_END = "TIME_PROFILE_TITLE_END";
    private static final String TIME_START_TIME = "TIME_START_TIME";
    private static final String TIME_END_TIME = "TIME_END_TIME";
    private static final String TIME_STATE = "TIME_STATE";
    private static final String TIME_DATE = "TIME_DATE";
    private static final String TIME_REPEAT = "TIME_REPEAT";
    private static final String TIME_DAYS = "TIME_DAYS";
    private static final String TIME_PROFILE_START = "TIME_PROFILE_START";
    private static final String TIME_PROFILE_END = "TIME_PROFILE_END";


    private static final String PROFILE_TABLE = "PROFILE_TABLE";
    private static final String PROFILE_P_KEY = "PROFILE_P_KEY";
    private static final String PROFILE_TITLE = "PROFILE_TITLE";
    private static final String RINGER_MODE = "RINGER_MODE";
    private static final String RINGER_LEVEL = "RINGER_LEVEL";
    private static final String MEDIA_LEVEL = "MEDIA_LEVEL";
    private static final String NOTIFICATION_LEVEL = "NOTIFICATION_LEVEL";
    private static final String SYSTEM_LEVEL = "SYSTEM_LEVEL";
    private static final String VIBRATE = "VIBRATE";
    private static final String TOUCH_SOUND = "TOUCH_SOUND";
    private static final String LOCK_SCREEN_SOUND = "LOCK_SCREEN_SOUND";
    private static final String DIAL_PAD_SOUND = "DIAL_PAD_SOUND";


    //
    private static final String PER_DAY_TABLE = "PER_DAY_TABLE";
    private static final String PRIMARY_ID = "PRIMARY_ID";
    private static final String STARTED_TIME = "STARTED_TIME";


    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + LOCATION_TABLE + "(LOCATION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "LOCATION_TITLE TEXT" +
                 ",LATITUDE TEXT" +
                ",LONGITUDE TEXT" +
                ",RADIUS TEXT" +
                ",GEO_FENCE_TYPE TEXT" +
                ",EXPIRATION_TIME TEXT" +
                ",LOCATION_STATE TEXT" +
                ",LOCATION_DATE TEXT" +
                ",LOCATION_PROFILE_ENTER TEXT" +
                ",LOCATION_PROFILE_EXIT TEXT)");

        db.execSQL("create table " + TIME_TABLE + "(TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TIME_TITLE TEXT" +
                ",TIME_PROFILE_TITLE_START TEXT" +
                ",TIME_PROFILE_TITLE_END TEXT" +
                ",TIME_START_TIME TEXT" +
                ",TIME_END_TIME TEXT" +
                ",TIME_STATE TEXT" +
                ",TIME_DATE TEXT" +
                ",TIME_REPEAT TEXT" +
                ",TIME_DAYS TEXT" +
                ",TIME_PROFILE_START TEXT" +
                ",TIME_PROFILE_END TEXT)");

        db.execSQL("create table " + PROFILE_TABLE + "(PROFILE_P_KEY INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PROFILE_TITLE TEXT" +
                ",RINGER_MODE TEXT" +
                ",RINGER_LEVEL TEXT" +
                ",MEDIA_LEVEL TEXT" +
                ",NOTIFICATION_LEVEL TEXT" +
                ",SYSTEM_LEVEL TEXT" +
                ",VIBRATE TEXT" +
                ",TOUCH_SOUND TEXT" +
                ",LOCK_SCREEN_SOUND TEXT" +
                ",DIAL_PAD_SOUND)");

        db.execSQL("create table " + PER_DAY_TABLE + "(PRIMARY_ID INTEGER PRIMARY KEY," +
                "STARTED_TIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + LOCATION_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS " + TIME_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS " + PROFILE_TABLE);
        db.execSQL(" DROP TABLE IF EXISTS " + PER_DAY_TABLE);
        onCreate(db);
    }

    // per day table
    public long insert(String startTime) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRIMARY_ID, 0);
        values.put(STARTED_TIME, startTime);
        return database.insert(PER_DAY_TABLE, null, values);
    }

    public long updatePerDay(String id, String startTime) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRIMARY_ID, 0);
        values.put(STARTED_TIME, startTime);
        return database.update(PER_DAY_TABLE, values, MyDatabase.PRIMARY_ID + "=?",
                new String[]{id});
    }

    public Cursor getPerDay()
    {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery("select * from " + PER_DAY_TABLE, null,
                null);

    }

    //location base table functions
    public long insert(String location_title , String latitude,
                       String longitude, String radius, String geoFenceType,
                       String expirationTime, String state, String date,
                       String enter_profile_id,String exit_profile_id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATION_TITLE, location_title);
         values.put(LATITUDE, latitude);
        values.put(LONGITUDE, longitude);
        values.put(RADIUS, radius);
        values.put(GEO_FENCE_TYPE, geoFenceType);
        values.put(EXPIRATION_TIME, expirationTime);
        values.put(LOCATION_STATE, state);
        values.put(LOCATION_DATE, date);
        values.put(LOCATION_PROFILE_ENTER, enter_profile_id);
        values.put(LOCATION_PROFILE_EXIT, exit_profile_id);
        return database.insert(LOCATION_TABLE, null, values);
    }

    public long updateLocation(String id, String location_title , String state,
                               String enter_profile_id,String exit_profile_id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATION_TITLE, location_title);
         values.put(LOCATION_STATE, state);
        values.put(LOCATION_PROFILE_ENTER, enter_profile_id);
        values.put(LOCATION_PROFILE_EXIT, exit_profile_id);
        return database.update(LOCATION_TABLE, values, MyDatabase.LOCATION_ID + "=?",
                new String[]{id});
    }

    public Cursor retrieveLocation(String id) {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery( "SELECT * FROM " + LOCATION_TABLE +
                        " WHERE LOCATION_ID LIKE '" + id + "'", null );


    }

    public Cursor retrieveLocation() {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery("select * from " + LOCATION_TABLE, null,
                null);
    }

    public void deleteLocation(String id) {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(LOCATION_TABLE, MyDatabase.LOCATION_ID + "=?",
                new String[]{id});
    }

    public void deleteLocation() {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(LOCATION_TABLE, null, null);
    }

    //time base table functions

    public long insertTimeTable(String time_title, String profileTitleStart, String profileTitleEnd,
                                String startTime, String endTime, String state, String date,
                                String repeat, String days, String start_profile_id,
                                String end_profile_id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME_TITLE, time_title);
        values.put(TIME_PROFILE_TITLE_START, profileTitleStart);
        values.put(TIME_PROFILE_TITLE_END, profileTitleEnd);
        values.put(TIME_START_TIME, startTime);
        values.put(TIME_END_TIME, endTime);
        values.put(TIME_STATE, state);
        values.put(TIME_DATE, date);
        values.put(TIME_REPEAT, repeat);
        values.put(TIME_DAYS, days);
        values.put(TIME_PROFILE_START, start_profile_id);
        values.put(TIME_PROFILE_END, end_profile_id);

        return database.insert(TIME_TABLE, null, values);
    }

    public long updateTimeTable(String id, String time_title, String profileTitleStart,
                                String profileTitleEnd, String startTime, String endTime,
                                String state, String date, String repeat, String days,
                                String start_profile_id, String end_profile_id)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME_TITLE, time_title);
        values.put(TIME_PROFILE_TITLE_START, profileTitleStart);
        values.put(TIME_PROFILE_TITLE_END, profileTitleEnd);
        values.put(TIME_START_TIME, startTime);
        values.put(TIME_END_TIME, endTime);
        values.put(TIME_STATE, state);
        values.put(TIME_DATE, date);
        values.put(TIME_REPEAT, repeat);
        values.put(TIME_DAYS, days);
        values.put(TIME_PROFILE_START, start_profile_id);
        values.put(TIME_PROFILE_END, end_profile_id);

        return database.update(TIME_TABLE, values, MyDatabase.TIME_ID + "=?",
                new String[]{id});

    }

    public Cursor retrieveTimeTable(String id) {
        SQLiteDatabase databaseWritable = getWritableDatabase();
          String selectQuery = "SELECT  * FROM " + TIME_TABLE + " WHERE TIME_ID  LIKE '" + id+"'";
        return databaseWritable.rawQuery( selectQuery, null );

    }

    public Cursor retrieveTimeTable() {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery("select * from " + TIME_TABLE, null,
                null);
    }

    public void deleteTimeTable(String id) {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(TIME_TABLE, MyDatabase.TIME_ID + "=?",
                new String[]{id});
    }

    public void deleteTimeTable() {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(TIME_TABLE, null, null);
    }


    //profile table functions
    public long insertProfile(String profile_title, String ringerMode, String ringerLevel,
                              String mediaLevel, String notificationLevel, String systemLevel
            , String vibrate, String touchSound, String LockScreenSound, String dialPedSound) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROFILE_TITLE, profile_title);
        values.put(RINGER_MODE, ringerMode);
        values.put(RINGER_LEVEL, ringerLevel);
        values.put(MEDIA_LEVEL, mediaLevel);
        values.put(NOTIFICATION_LEVEL, notificationLevel);
        values.put(SYSTEM_LEVEL, systemLevel);
        values.put(VIBRATE, vibrate);
        values.put(TOUCH_SOUND, touchSound);
        values.put(LOCK_SCREEN_SOUND, LockScreenSound);
        values.put(DIAL_PAD_SOUND, dialPedSound);

        return database.insert(PROFILE_TABLE, null, values);
    }

    public long updateProfile(String id, String profile_title, String ringerMode, String ringerLevel,
                              String mediaLevel, String notificationLevel, String systemLevel
            , String vibrate, String touchSound, String LockScreenSound, String dialPedSound) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROFILE_TITLE, profile_title);
        values.put(RINGER_MODE, ringerMode);
        values.put(RINGER_LEVEL, ringerLevel);
        values.put(MEDIA_LEVEL, mediaLevel);
        values.put(NOTIFICATION_LEVEL, notificationLevel);
        values.put(SYSTEM_LEVEL, systemLevel);
        values.put(DIAL_PAD_SOUND, dialPedSound);
        values.put(LOCK_SCREEN_SOUND, LockScreenSound);
        values.put(TOUCH_SOUND, touchSound);
        values.put(VIBRATE, vibrate);

        return database.update(PROFILE_TABLE, values, MyDatabase.PROFILE_P_KEY + "=?",
                new String[]{id});
    }


    public Cursor retrieveProfile(String id) {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery( "SELECT * FROM " + PROFILE_TABLE +
                " WHERE PROFILE_P_KEY LIKE '" + id + "'", null );

    }

    public Cursor retrieveProfile() {
        SQLiteDatabase databaseWritable = getWritableDatabase();
        return databaseWritable.rawQuery("select * from " + PROFILE_TABLE, null,
                null);
    }

    public void deleteProfile(String id) {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(PROFILE_TABLE, MyDatabase.PROFILE_P_KEY + "=?",
                new String[]{id});
    }

    public void deleteProfile() {
        SQLiteDatabase database = getWritableDatabase();
        int delete = database.delete(PROFILE_TABLE, null, null);
    }

    public long retrieveRowsAmountLocation() {

        SQLiteDatabase databaseReadable = getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(databaseReadable, LOCATION_TABLE);
        databaseReadable.close();
        return count;
    }

    public long retrieveRowsAmountTimeTable() {

        SQLiteDatabase databaseReadable = getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(databaseReadable, TIME_TABLE);
        databaseReadable.close();
        return count;
    }

    public long retrieveRowsAmountProfiles() {

        SQLiteDatabase databaseReadable = getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(databaseReadable, PROFILE_TABLE);
        databaseReadable.close();
        return count;
    }


}
