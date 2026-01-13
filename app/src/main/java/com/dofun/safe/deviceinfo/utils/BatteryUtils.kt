package com.dofun.safe.deviceinfo.utils

import android.content.Context
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.R
import com.dofun.safe.deviceinfo.utils.Constants

object BatteryUtils {
    fun getBatteryInfo(context: Context, list: MutableList<Pair<String, String>>) {
        try {
            val batteryStatus = context.registerReceiver(null, IntentFilter("android.intent.action.BATTERY_CHANGED"))
            if (batteryStatus != null) {
                val level = batteryStatus.getIntExtra("level", -1)
                val scale = batteryStatus.getIntExtra("scale", -1)
                var batteryLevel = -1.0
                if (level != -1 && scale != -1) {
                    batteryLevel = DecimalUtils.divide(level.toDouble(), scale.toDouble())
                }
                val status = batteryStatus.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1)
                val plugState = batteryStatus.getIntExtra("plugged", -1)
                val health = batteryStatus.getIntExtra("health", -1)
                val present = batteryStatus.getBooleanExtra("present", false)
                val technology = batteryStatus.getStringExtra("technology")
                val temperature = batteryStatus.getIntExtra("temperature", -1)
                val voltage = batteryStatus.getIntExtra("voltage", -1)
                list.add(Pair(context.getString(R.string.battery_level), (DecimalUtils.mul(batteryLevel, 100.0)).toString() + "%"))
                list.add(Pair(context.getString(R.string.battery_health), batteryHealth(health)))
                list.add(Pair(context.getString(R.string.battery_status), batteryStatus(status)))
                list.add(Pair(context.getString(R.string.battery_power_source), batteryPlugged(plugState)))
                list.add(Pair(context.getString(R.string.battery_technology), technology ?: Constants.UNKNOWN))
                list.add(Pair(context.getString(R.string.battery_present), present.toString()))
                list.add(Pair(context.getString(R.string.battery_temperature), (temperature / 10).toString() + " ℃"))
                if (voltage > 1000) {
                    list.add(Pair(context.getString(R.string.battery_voltage), (voltage / 1000.0f).toString() + "V"))
                } else {
                    list.add(Pair(context.getString(R.string.battery_voltage), voltage.toString() + "V"))
                }
                list.add(Pair(context.getString(R.string.battery_average), getAverageCurrent(context).toString()))
                list.add(Pair(context.getString(R.string.battery_power_profile), getBatteryCapacity(context)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAverageCurrent(context: Context): Int {
        return try {
            val batteryManager = context.getSystemService("batterymanager") as BatteryManager
            batteryManager.getIntProperty(3)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getBatteryCapacity(context: Context): String {
        var batteryCapacity = 0.0
        try {
            val powerProfile = Class.forName("com.android.internal.os.PowerProfile")
                .getConstructor(Context::class.java)
                .newInstance(context)
            batteryCapacity = Class.forName("com.android.internal.os.PowerProfile")
                .getMethod("getBatteryCapacity")
                .invoke(powerProfile) as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "$batteryCapacity mAh"
    }

    private fun batteryHealth(status: Int): String {
        return when (status) {
            1 -> "Unknown"
            2 -> "Good"
            3 -> "Overheat"
            4 -> "Dead"
            5 -> "OverVoltage"
            6 -> "Unspecified"
            7 -> "Cold"
            else -> Constants.UNKNOWN
        }
    }

    private fun batteryStatus(status: Int): String {
        return when (status) {
            1 -> "Unknown"
            2 -> "Charging"
            3 -> "DisCharging"
            4 -> "NotCharging"
            5 -> "Full"
            else -> Constants.UNKNOWN
        }
    }

    private fun batteryPlugged(status: Int): String {
        return when (status) {
            1 -> "AC"
            2 -> "USB"
            4 -> "Wireless"
            else -> Constants.UNKNOWN
        }
    }
}
