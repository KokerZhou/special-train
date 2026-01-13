package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class HardwareInfo {
    public static JSONObject getHardwareInfoJson(Context context) {
        return CommandUtils.convertToJson(getHardwareInfo(context));
    }

    public static List<Pair<String, String>> getHardwareInfo(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        Sensor gyroscope = sensorManager.getDefaultSensor(4);
        list.add(new Pair<>("gyroscope", gyroscope.getName()));
        Sensor magnetic = sensorManager.getDefaultSensor(2);
        list.add(new Pair<>("magnetic", magnetic.getName()));
        Sensor accelerometer = sensorManager.getDefaultSensor(1);
        list.add(new Pair<>("accelerometer", accelerometer.getName()));
        return list;
    }
}
