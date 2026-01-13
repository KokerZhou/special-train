package com.dofun.safe.deviceinfo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.bean.CpuBean;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/* loaded from: classes6.dex */
public class SocUtils {
    public static String getSocInfo() {
        String socStr = CommandUtils.execute("getprop ro.board.platform");
        if (!TextUtils.isEmpty(socStr)) {
            return socStr;
        }
        String socStr2 = CommandUtils.execute("getprop ro.hardware");
        if (TextUtils.isEmpty(socStr2)) {
            return CommandUtils.execute("getprop ro.boot.hardware");
        }
        return socStr2;
    }

    public static void setCpuInfo(List<Pair<String, String>> list) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            CpuBean bean = new CpuBean();
            HashSet<String> parts = new HashSet<>();
            HashSet<String> implementer = new HashSet<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String result = line.toLowerCase();
                    FileLogger.d("soc-", "CPU: " + result);
                    String[] split = result.split(":\\s+", 2);
                    if (split[0].startsWith("cpu part")) {
                        parts.add(split[1]);
                    } else if (split[0].startsWith("hardware")) {
                        bean.setHardware(split[1]);
                    } else if (split[0].startsWith("features")) {
                        bean.setFeatures(split[1]);
                    } else if (split[0].startsWith("cpu implementer")) {
                        implementer.add(split[1]);
                    }
                } else {
                    bean.setParts((String[]) parts.toArray(new String[0]));
                    bean.setImplementers((String[]) implementer.toArray(new String[0]));
                    list.add(new Pair<>("parts", parts.toString()));
                    list.add(new Pair<>("implementer", implementer.toString()));
                    list.add(new Pair<>("hardware", bean.getHardware()));
                    list.add(new Pair<>("features", bean.getFeatures()));
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getGPUInfo(Context context, List<Pair<String, String>> list) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService("activity");
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        list.add(new Pair<>("GlEsVersion", info.getGlEsVersion()));
        list.add(new Pair<>("reqGlEsVersion", info.reqGlEsVersion + ""));
        list.add(new Pair<>("reqInputFeatures", info.reqInputFeatures + ""));
        list.add(new Pair<>("reqKeyboardType", info.reqKeyboardType + ""));
        list.add(new Pair<>("reqNavigation", info.reqNavigation + ""));
        list.add(new Pair<>("reqTouchScreen", info.reqTouchScreen + ""));
        list.add(new Pair<>("describeContents", info.describeContents() + ""));
    }
}
