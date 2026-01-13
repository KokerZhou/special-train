package com.dofun.safe.deviceinfo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import androidx.core.app.NotificationCompat;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.R;
import java.util.List;

/* loaded from: classes6.dex */
public class BatteryUtils {
    public static void getBatteryInfo(Context context, List<Pair<String, String>> list) {
        try {
            Intent batteryStatus = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (batteryStatus != null) {
                int level = batteryStatus.getIntExtra("level", -1);
                int scale = batteryStatus.getIntExtra("scale", -1);
                double batteryLevel = -1.0d;
                if (!(level == -1 || scale == -1)) {
                    batteryLevel = DecimalUtils.divide(Double.valueOf(level), Double.valueOf(scale)).doubleValue();
                }
                int status = batteryStatus.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
                int plugState = batteryStatus.getIntExtra("plugged", -1);
                int health = batteryStatus.getIntExtra("health", -1);
                boolean present = batteryStatus.getBooleanExtra("present", false);
                String technology = batteryStatus.getStringExtra("technology");
                int temperature = batteryStatus.getIntExtra("temperature", -1);
                int voltage = batteryStatus.getIntExtra("voltage", -1);
                list.add(new Pair<>(context.getString(R.string.battery_level), DecimalUtils.mul(Double.valueOf(batteryLevel), Double.valueOf(100.0d)) + "%"));
                list.add(new Pair<>(context.getString(R.string.battery_health), batteryHealth(health)));
                list.add(new Pair<>(context.getString(R.string.battery_status), batteryStatus(status)));
                list.add(new Pair<>(context.getString(R.string.battery_power_source), batteryPlugged(plugState)));
                list.add(new Pair<>(context.getString(R.string.battery_technology), technology));
                list.add(new Pair<>(context.getString(R.string.battery_present), present + ""));
                list.add(new Pair<>(context.getString(R.string.battery_temperature), (temperature / 10) + " ℃"));
                if (voltage > 1000) {
                    list.add(new Pair<>(context.getString(R.string.battery_voltage), (voltage / 1000.0f) + "V"));
                } else {
                    list.add(new Pair<>(context.getString(R.string.battery_voltage), voltage + "V"));
                }
                list.add(new Pair<>(context.getString(R.string.battery_average), getAverageCurrent(context) + ""));
                list.add(new Pair<>(context.getString(R.string.battery_power_profile), getBatteryCapacity(context)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getAverageCurrent(Context context) {
        try {
            BatteryManager batteryManager = (BatteryManager) context.getSystemService("batterymanager");
            return batteryManager.getIntProperty(3);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getBatteryCapacity(Context context) {
        double batteryCapacity = 0.0d;
        try {
            Object mPowerProfile = Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(context);
            batteryCapacity = ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getBatteryCapacity", new Class[0]).invoke(mPowerProfile, new Object[0])).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity + " mAh";
    }

    private static String batteryHealth(int status) {
        switch (status) {
            case 1:
                return "Unknown";
            case 2:
                return "Good";
            case 3:
                return "Overheat";
            case 4:
                return "Dead";
            case 5:
                return "OverVoltage";
            case 6:
                return "Unspecified";
            case 7:
                return "Cold";
            default:
                return Constants.UNKNOWN;
        }
    }

    private static String batteryStatus(int status) {
        switch (status) {
            case 1:
                return "Unknown";
            case 2:
                return "Charging";
            case 3:
                return "DisCharging";
            case 4:
                return "NotCharging";
            case 5:
                return "Full";
            default:
                return Constants.UNKNOWN;
        }
    }

    private static String batteryPlugged(int status) {
        switch (status) {
            case 1:
                return "AC";
            case 2:
                return "USB";
            case 3:
            default:
                return Constants.UNKNOWN;
            case 4:
                return "Wireless";
        }
    }
}
