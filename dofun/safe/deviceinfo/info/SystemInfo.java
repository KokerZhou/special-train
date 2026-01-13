package com.dofun.safe.deviceinfo.info;

import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.R;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.FileUtils;
import com.dofun.safe.deviceinfo.utils.SocUtils;
import com.dofun.safe.deviceinfo.utils.SystemConfigUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class SystemInfo {
    public static JSONObject getSystemInfoJson(Context context) {
        return CommandUtils.convertToJson(getSystemInfo((Activity) context));
    }

    public static List<Pair<String, String>> getSystemInfo(Activity activity) {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        Context context = activity.getApplicationContext();
        list.add(new Pair<>(context.getString(R.string.system_manufacture), Build.MANUFACTURER));
        list.add(new Pair<>(context.getString(R.string.system_model), Build.MODEL));
        list.add(new Pair<>(context.getString(R.string.system_brand), Build.BRAND));
        list.add(new Pair<>(context.getString(R.string.system_release), Build.VERSION.RELEASE));
        list.add(new Pair<>(context.getString(R.string.system_api), Build.VERSION.SDK_INT + ""));
        list.add(new Pair<>(context.getString(R.string.system_code_name), Build.VERSION.CODENAME));
        list.add(new Pair<>(context.getString(R.string.system_device), Build.DEVICE));
        list.add(new Pair<>(context.getString(R.string.system_product), Build.PRODUCT));
        list.add(new Pair<>(context.getString(R.string.system_board), Build.BOARD));
        list.add(new Pair<>(context.getString(R.string.system_platform), SocUtils.getSocInfo()));
        list.add(new Pair<>(context.getString(R.string.system_build), Build.ID));
        list.add(new Pair<>(context.getString(R.string.system_vm), System.getProperty("java.vm.version")));
        list.add(new Pair<>(context.getString(R.string.system_security), CommandUtils.getProperty("ro.build.version.security_patch")));
        list.add(new Pair<>(context.getString(R.string.system_baseband), CommandUtils.getProperty("gsm.version.baseband")));
        list.add(new Pair<>(context.getString(R.string.system_build_type), Build.TYPE));
        list.add(new Pair<>(context.getString(R.string.system_tags), Build.TAGS));
        list.add(new Pair<>(context.getString(R.string.system_incremental), Build.VERSION.INCREMENTAL));
        list.add(new Pair<>(context.getString(R.string.system_description), CommandUtils.getProperty("ro.build.description")));
        list.add(new Pair<>(context.getString(R.string.system_fingerprint), Build.FINGERPRINT));
        PackageManager pm = context.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        String featureCount = features != null ? String.valueOf(features.length) : "0";
        list.add(new Pair<>(context.getString(R.string.system_device_features), featureCount));
        list.add(new Pair<>(context.getString(R.string.system_language), SystemConfigUtils.getCurrentLanguage(context)));
        list.add(new Pair<>(context.getString(R.string.system_timezone), SystemConfigUtils.getCurrentTimeZone()));
        list.add(new Pair<>(context.getString(R.string.system_uptime), SystemConfigUtils.getSystemUpdate()));
        list.add(new Pair<>("displayId", Build.DISPLAY));
        list.add(new Pair<>("bootloader", Build.BOOTLOADER));
        list.add(new Pair<>("hardware", Build.HARDWARE));
        list.add(new Pair<>("buildUser", Build.USER));
        list.add(new Pair<>("buildHost", Build.HOST));
        list.add(new Pair<>("bootId", FileUtils.readFile("/proc/sys/kernel/random/boot_id")));
        list.add(new Pair<>("UUID", FileUtils.readFile("/proc/sys/kernel/random/uuid")));
        list.add(new Pair<>("characteristics", CommandUtils.getProperty("ro.build.characteristics")));
        return list;
    }
}
