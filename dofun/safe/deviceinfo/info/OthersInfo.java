package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.WebSettings;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.R;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.Constants;
import com.dofun.safe.deviceinfo.utils.DensityUtils;
import com.dofun.safe.deviceinfo.utils.FileUtils;
import com.dofun.safe.deviceinfo.utils.SdUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class OthersInfo {
    public static JSONObject getOthersInfoJson(Context context) {
        return CommandUtils.convertToJson(getOthersInfo(context));
    }

    public static List<Pair<String, String>> getOthersInfo(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("UA", getDefaultUserAgent(context)));
        list.add(new Pair<>("poolSize", FileUtils.readFile("/proc/sys/kernel/random/poolsize")));
        list.add(new Pair<>("entropyAvail", FileUtils.readFile("/proc/sys/kernel/random/entropy_avail")));
        list.add(new Pair<>("writeThreshold", FileUtils.readFile("/proc/sys/kernel/random/write_wakeup_threshold")));
        list.add(new Pair<>("secs", FileUtils.readFile("/proc/sys/kernel/random/urandom_min_reseed_secs")));
        list.add(new Pair<>("country", Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()));
        list.add(new Pair<>(context.getString(R.string.system_refresh_rate), DensityUtils.getRefreshRate(context) + "Hz"));
        list.add(new Pair<>("dpi", DensityUtils.getDensityDpi(context) + ""));
        list.add(new Pair<>("density", DensityUtils.getDensity(context) + ""));
        list.add(new Pair<>("width * height", DensityUtils.getScreenWidth(context) + " X " + DensityUtils.getScreenHeight(context)));
        list.add(new Pair<>("widthDp * heightDp", DensityUtils.getScreenWidthWithDp(context) + " X " + DensityUtils.getScreenHeightWithDp(context)));
        list.add(new Pair<>("statusBarHeight", DensityUtils.getStatusBarHeight(context) + ""));
        list.add(new Pair<>("navigationBarHeight", DensityUtils.getNavigationBarHeight(context) + ""));
        try {
            list.add(new Pair<>("screenBrightness", Settings.System.getInt(context.getContentResolver(), "screen_brightness") + ""));
            boolean z = false;
            list.add(new Pair<>("screenBrightnessAuto", (Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode") == 1) + ""));
            StringBuilder sb = new StringBuilder();
            if (Settings.System.getInt(context.getContentResolver(), "accelerometer_rotation") == 1) {
                z = true;
            }
            list.add(new Pair<>("screenOrientationAuto", sb.append(z).append("").toString()));
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        list.add(new Pair<>("hideStatusBar", DensityUtils.hideStatusBar(context) + ""));
        list.add(new Pair<>("hasNavigationBar", DensityUtils.hasNavigationBar(context) + ""));
        list.add(new Pair<>("sdCardEnable", SdUtils.isMounted() + ""));
        return list;
    }

    private static String getDefaultUserAgent(Context context) {
        String ua = null;
        try {
            ua = System.getProperty("http.agent");
            if (TextUtils.isEmpty(ua)) {
                Method localMethod = WebSettings.class.getDeclaredMethod("getDefaultUserAgent", Context.class);
                ua = (String) localMethod.invoke(WebSettings.class, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(ua) ? Constants.UNKNOWN : ua;
    }
}
