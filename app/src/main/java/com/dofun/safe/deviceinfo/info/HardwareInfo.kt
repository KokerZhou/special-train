package com.dofun.safe.deviceinfo.info

import android.content.Context
import android.hardware.SensorManager
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.utils.CommandUtils
import org.json.JSONObject

object HardwareInfo {
    @JvmStatic
    fun getHardwareInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getHardwareInfo(context))
    }

    @JvmStatic
    fun getHardwareInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        val sensorManager = context.getSystemService("sensor") as SensorManager
        val gyroscope = sensorManager.getDefaultSensor(4)
        list.add(Pair("gyroscope", gyroscope.name))
        val magnetic = sensorManager.getDefaultSensor(2)
        list.add(Pair("magnetic", magnetic.name))
        val accelerometer = sensorManager.getDefaultSensor(1)
        list.add(Pair("accelerometer", accelerometer.name))
        return list
    }
}
