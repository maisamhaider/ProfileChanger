package com.example.profilechanger.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.wifi.WifiManager;

public class BooleansUtils extends ContextWrapper {

    Context context;
    private final WifiManager wifiManager;

    public BooleansUtils(Context base) {
        super(base);
        context = base;
        wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }


    public boolean isWifiEnable() {

        return wifiManager.isWifiEnabled();
    }
    public void setWifiOnOff(boolean trueFalse) {

        wifiManager.setWifiEnabled(trueFalse);

    }

}
