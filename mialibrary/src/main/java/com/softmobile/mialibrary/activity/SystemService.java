package com.softmobile.mialibrary.activity;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by miahuang on 2015/10/1.
 */
public class SystemService {
    public static LocationManager getLocationManager(Context context) {
        return ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE));
    }

    public static boolean isWifiEnable(Context context) {
        return ((WifiManager)context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static boolean isOnline(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager)context
                        .getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
