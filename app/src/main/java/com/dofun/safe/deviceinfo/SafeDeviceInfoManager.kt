package com.dofun.safe.deviceinfo

import android.app.Activity
import android.content.Context
import com.dofun.safe.deviceinfo.info.BatteryInfo
import com.dofun.safe.deviceinfo.info.CameraInfo
import com.dofun.safe.deviceinfo.info.DebugInfo
import com.dofun.safe.deviceinfo.info.HardwareInfo
import com.dofun.safe.deviceinfo.info.NetWorkInfo
import com.dofun.safe.deviceinfo.info.OthersInfo
import com.dofun.safe.deviceinfo.info.SOCInfo
import com.dofun.safe.deviceinfo.info.StoreInfo
import com.dofun.safe.deviceinfo.info.SystemInfo
import com.dofun.safe.deviceinfo.utils.FileLogger
import org.json.JSONException
import org.json.JSONObject

class SafeDeviceInfoManager private constructor() {
    fun init(context: Context) {
        FileLogger.init(context.applicationContext)
    }

    fun collectAllInfo(ctx: Activity): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("system", SystemInfo.getSystemInfoJson(ctx))
            jsonObject.put("battery", BatteryInfo.getBatteryInfoJson(ctx))
            jsonObject.put("soc", SOCInfo.getSOCInfoJson(ctx))
            jsonObject.put("store", StoreInfo.getStoreInfoJson(ctx))
            jsonObject.put("camera", CameraInfo.getCameraInfoJson(ctx))
            jsonObject.put("debug", DebugInfo.getDebugInfoJson(ctx))
            jsonObject.put("sensor", HardwareInfo.getHardwareInfoJson(ctx))
            jsonObject.put("network", NetWorkInfo.getNetWorkInfoJson(ctx))
            jsonObject.put("other", OthersInfo.getOthersInfoJson(ctx))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }

    companion object {
        @Volatile
        private var instance: SafeDeviceInfoManager? = null

        @JvmStatic
        fun getInstance(): SafeDeviceInfoManager {
            return instance ?: synchronized(SafeDeviceInfoManager::class.java) {
                instance ?: SafeDeviceInfoManager().also { instance = it }
            }
        }
    }
}
