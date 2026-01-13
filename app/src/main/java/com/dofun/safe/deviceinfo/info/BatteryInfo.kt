package com.dofun.safe.deviceinfo.info

import android.content.Context
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.utils.BatteryUtils
import com.dofun.safe.deviceinfo.utils.CommandUtils
import org.json.JSONObject

object BatteryInfo {
    @JvmStatic
    fun getBatteryInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        BatteryUtils.getBatteryInfo(context, list)
        return list
    }

    @JvmStatic
    fun getBatteryInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getBatteryInfo(context))
    }
}
