package com.example.profilechanger.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

import com.example.profilechanger.annotations.MyAnnotations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
    Context context;

    public TimeUtil() {
    }

    public TimeUtil(Context context) {
        this.context = context;
    }

    public long getFromNow(long millis) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();
//        SimpleDateFormat format1 = new SimpleDateFormat("dd:MM:yyyy h:mm:a");;
//        String date1 =format1.format(calendar.getTime());
        calendar.setTimeInMillis(nowMillis + millis);
//        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy h:mm:a");
//        String date = format.format(calendar.getTime());
        return calendar.getTimeInMillis();
    }

    public long getNowMillis() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public long getMillisFromFormattedDate(String date, String format) {

        Date outDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat(format);
        try {
            outDate = dateFormat.parse(date);
            String d =  dateFormat.format(outDate.getTime());

        } catch (Exception e) {
            e.getStackTrace();
        }

        return outDate != null ? outDate.getTime() : 0;
    }

    public long getCurrentTime(String format) {
        return getMillisFromFormattedDate(getCurrentFormattedDate() + " "
                + getCurrentFormattedTime(), format);
    }

    public long get12AmMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM, 1);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }

    public String getCurrentFormattedTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        return format.format(calendar.getTime());
    }

    public String getCurrentFormattedTimePlusHours(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        String d = format.format(calendar.getTime());
        return format.format(calendar.getTime());
    }
    public String getTimePlusHours(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        String d = format.format(calendar.getTime());
        return format.format(calendar.getTime());
    }

    public String getCurrentFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");

        return format.format(calendar.getTime());
    }

    public String getCurrentFormattedDatePlusHour(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");

        return format.format(calendar.getTime());
    }

    public long getMilliDateAndTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy h:mm a");

        format.setCalendar(calendar);


        return format.getCalendar().getTimeInMillis();
    }

    public String getFormattedDateAndTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy h:mm a");

        return format.format(calendar.getTime());
    }

    public String getFormattedTimeJust(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

        return format.format(calendar.getTime());
    }

    public String getFormattedDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");

        return format.format(calendar.getTime());
    }

    public String getTomorrowDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
        Date date = calendar.getTime();
        return format.format(date.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public String getToday() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("EE").format(calendar.getTime());
    }

    public String getTomorrowDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat("EE").format(calendar.getTime());
    }

}
