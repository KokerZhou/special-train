package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.DebugUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class DebugInfo {
    public static JSONObject getDebugInfoJson(Context context) {
        return CommandUtils.convertToJson(getDebugInfo(context));
    }

    public static List<Pair<String, String>> getDebugInfo(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("debugOpen", DebugUtils.isOpenDebug(context) + ""));
        list.add(new Pair<>("usbDebugStatus", DebugUtils.getUsbDebugStatus()));
        list.add(new Pair<>("debugVersion", DebugUtils.isDebugVersion(context) + ""));
        list.add(new Pair<>("debugConnected", DebugUtils.isDebugConnected() + ""));
        return list;
    }
}
