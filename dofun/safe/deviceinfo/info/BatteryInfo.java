package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.utils.BatteryUtils;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class BatteryInfo {
    public static List<Pair<String, String>> getBatteryInfo(Context context) {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        BatteryUtils.getBatteryInfo(context, list);
        return list;
    }

    public static JSONObject getBatteryInfoJson(Context context) {
        return CommandUtils.convertToJson(getBatteryInfo(context));
    }
}
