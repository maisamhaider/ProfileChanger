package com.example.profilechanger.utils;

import android.content.Context;
import android.os.Build;

public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

//    public void showDialog(String title,String message)
//    {
//        AlertDialog.Builder adb = new AlertDialog.Builder(context)
//                .setTitle(title).setMessage(message).setCancelable(true).setMessage()
//    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public boolean isAboveP()
    {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.P;

    }
}
