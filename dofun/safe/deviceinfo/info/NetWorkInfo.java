package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.NetWorkUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class NetWorkInfo {
    public static JSONObject getNetWorkInfoJson(Context context) {
        return CommandUtils.convertToJson(getNetWorkInfo(context));
    }

    public static List<Pair<String, String>> getNetWorkInfo(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        getNetWorkStatus(context, list);
        return list;
    }

    private static void getNetWorkStatus(Context context, List<Pair<String, String>> list) {
        list.add(new Pair<>("netAvailability", NetWorkUtils.isNetworkConnected(context) + ""));
        list.add(new Pair<>("mobileAvailability", NetWorkUtils.isMobileEnabled(context) + ""));
        list.add(new Pair<>("wifiAvailability", NetWorkUtils.isWifi(context) + ""));
        list.add(new Pair<>("netType", NetWorkUtils.getNetWorkType(context)));
    }
}
