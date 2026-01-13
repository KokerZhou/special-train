package com.dofun.safe.deviceinfo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;

/* loaded from: classes6.dex */
public class NetWorkUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isMobileEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            method.setAccessible(true);
            return ((Boolean) method.invoke(cm, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isWifi(Context context) {
        NetworkInfo networkInfo;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService("connectivity");
        if (manager == null || (networkInfo = manager.getNetworkInfo(1)) == null || !networkInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public static String getNetWorkType(Context context) {
        if (!isNetworkConnected(context)) {
            return "NONE";
        }
        if (isWifi(context)) {
            return "WIFI";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case 1:
                    return "GPRS";
                case 2:
                    return "EDGE";
                case 3:
                    return "UMTS";
                case 4:
                    return "CDMA";
                case 5:
                    return "EVDO_0";
                case 6:
                    return "EVDO_A";
                case 7:
                    return "1xRTT";
                case 8:
                    return "HSDPA";
                case 9:
                    return "HSUPA";
                case 10:
                    return "HSPA";
                case 11:
                    return "IDEN";
                case 12:
                    return "EVDO_B";
                case 13:
                    return "LTE";
                case 14:
                    return "EHRPD";
                case 15:
                    return "HSPAP";
                case 20:
                    return "5G";
            }
        }
        return "NONE";
    }
}
