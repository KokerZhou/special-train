package com.dofun.safe.deviceinfo.utils;

import android.content.Context;
import android.os.SystemClock;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes6.dex */
public class SystemConfigUtils {
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, 0);
    }

    public static String getCurrentLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String str = language + "_" + country;
        return locale.getDisplayLanguage();
    }

    public static String getSystemUpdate() {
        long nanoTime = SystemClock.elapsedRealtime();
        long day = nanoTime / 86400000;
        return day > 0 ? (nanoTime / 86400000) + " days " + ((nanoTime % 86400000) / 3600000) + ":" + (((nanoTime % 86400000) % 3600000) / 60000) : ((nanoTime % 86400000) / 3600000) + ":" + (((nanoTime % 86400000) % 3600000) / 60000);
    }
}
