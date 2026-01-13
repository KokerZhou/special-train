package com.dofun.safe.deviceinfo;

import android.app.Activity;
import android.content.Context;
import com.dofun.safe.deviceinfo.info.BatteryInfo;
import com.dofun.safe.deviceinfo.info.CameraInfo;
import com.dofun.safe.deviceinfo.info.DebugInfo;
import com.dofun.safe.deviceinfo.info.HardwareInfo;
import com.dofun.safe.deviceinfo.info.NetWorkInfo;
import com.dofun.safe.deviceinfo.info.OthersInfo;
import com.dofun.safe.deviceinfo.info.SOCInfo;
import com.dofun.safe.deviceinfo.info.StoreInfo;
import com.dofun.safe.deviceinfo.info.SystemInfo;
import com.dofun.safe.deviceinfo.utils.FileLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes9.dex */
public class SafeDeviceInfoManager {
    private static SafeDeviceInfoManager instance;

    private SafeDeviceInfoManager() {
    }

    public static synchronized SafeDeviceInfoManager getInstance() {
        SafeDeviceInfoManager safeDeviceInfoManager;
        synchronized (SafeDeviceInfoManager.class) {
            if (instance == null) {
                instance = new SafeDeviceInfoManager();
            }
            safeDeviceInfoManager = instance;
        }
        return safeDeviceInfoManager;
    }

    public void init(Context context) {
        FileLogger.init(context.getApplicationContext());
    }

    public JSONObject collectAllInfo(Activity ctx) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("system", SystemInfo.getSystemInfoJson(ctx));
            jsonObject.put("battery", BatteryInfo.getBatteryInfoJson(ctx));
            jsonObject.put("soc", SOCInfo.getSOCInfoJson(ctx));
            jsonObject.put("store", StoreInfo.getStoreInfoJson(ctx));
            jsonObject.put("camera", CameraInfo.getCameraInfoJson(ctx));
            jsonObject.put("debug", DebugInfo.getDebugInfoJson(ctx));
            jsonObject.put("sensor", HardwareInfo.getHardwareInfoJson(ctx));
            jsonObject.put("network", NetWorkInfo.getNetWorkInfoJson(ctx));
            jsonObject.put("other", OthersInfo.getOthersInfoJson(ctx));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
