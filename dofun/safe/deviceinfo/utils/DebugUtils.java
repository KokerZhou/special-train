package com.dofun.safe.deviceinfo.utils;

import android.content.Context;
import android.os.Debug;
import android.provider.Settings;

/* loaded from: classes6.dex */
public class DebugUtils {
    public static boolean isOpenDebug(Context context) {
        try {
            return Settings.Secure.getInt(context.getContentResolver(), "adb_enabled", 0) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDebugVersion(Context context) {
        try {
            return (context.getApplicationInfo().flags & 2) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isDebugConnected() {
        try {
            return Debug.isDebuggerConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getUsbDebugStatus() {
        return CommandUtils.execute("getprop init.svc.adbd");
    }
}
