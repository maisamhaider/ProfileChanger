package com.example.profilechanger.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.profilechanger.R;
import com.example.profilechanger.annotations.PermissionCodes;

import java.util.Objects;

public class Permissions {

    Context context;

    public Permissions(Context context) {
        this.context = context;
    }

    public boolean locationPer() {
        int locationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int courseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int bLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        boolean QAndAbove = locationPermission == PackageManager.PERMISSION_GRANTED
                && courseLocationPermission == PackageManager.PERMISSION_GRANTED
                && bLocationPermission == PackageManager.PERMISSION_GRANTED;

        boolean belowQ = locationPermission == PackageManager.PERMISSION_GRANTED
                && courseLocationPermission == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return QAndAbove;
        } else
            return belowQ;
    }

    public boolean permission() {
        int locationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int courseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int bLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        boolean QAndAbove = locationPermission == PackageManager.PERMISSION_GRANTED
                && courseLocationPermission == PackageManager.PERMISSION_GRANTED
                && bLocationPermission == PackageManager.PERMISSION_GRANTED;

        boolean belowQ = locationPermission == PackageManager.PERMISSION_GRANTED
                && courseLocationPermission == PackageManager.PERMISSION_GRANTED;


        String[] manifestPermissionArray = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION};

        String[] QArray = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION};


        //if device is android 10 or higher we need to take background location permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context
                    , Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                backgroundPermissionDialog("Permission", "without background" +
                        " location permission app can not access your" +
                        " entrance and exit from/to target location");

            }
            if (QAndAbove) {
                return true;
            } else {
                ActivityCompat.requestPermissions(((Activity) context),
                        QArray, PermissionCodes.REQ_CODE);
                return false;
            }
        } else if (belowQ) {
            return true;
        } else
            ActivityCompat.requestPermissions(((Activity) context),
                    manifestPermissionArray, PermissionCodes.REQ_CODE);
        return false;

    }

    public void backgroundPermissionDialog(String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context)
                .setTitle(title).setMessage(message).setCancelable(true);
        adb.setPositiveButton(R.string.allow, (dialog, which) -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions((Activity) context, new String[]
                                {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PermissionCodes.REQ_CODE);
                dialog.dismiss();
            }


        }).setNegativeButton(R.string.decline, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = adb.create();
        dialog.show();
    }


    public void openAndroidPermissionsMenu(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, PermissionCodes.MANGE_SETTINGS);
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    PermissionCodes.CODE_WRITE_SETTINGS_PERMISSION);
        }
    }

    public void doNoDisturbPermissionDialog() {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context))
                .setCancelable(false)
                .setTitle(R.string.do_not_disturb)
                .setMessage(R.string.do_not_disturb_permission_text)
                .setPositiveButton(R.string.allow, (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                            // Check if the notification policy access has been granted for the app.
                            Intent intent =
                                    new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            ((Activity) context).startActivityForResult(intent,
                                    PermissionCodes.DO_NOT_DISTURB);
                        }
                    }
                }).setNegativeButton(R.string.decline, (dialog, which) -> {

                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void overlayAppPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            ((Activity) context).startActivityForResult(intent,
                    PermissionCodes.DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }
}
