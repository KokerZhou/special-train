package com.dofun.safe.deviceinfo.info

import android.app.Activity
import android.content.Context
import android.content.pm.FeatureInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.R
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.Constants
import com.dofun.safe.deviceinfo.utils.FileUtils
import com.dofun.safe.deviceinfo.utils.SocUtils
import com.dofun.safe.deviceinfo.utils.SystemConfigUtils
import org.json.JSONObject

object SystemInfo {
    @JvmStatic
    fun getSystemInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getSystemInfo(context as Activity))
    }

    @JvmStatic
    fun getSystemInfo(activity: Activity): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        val context = activity.applicationContext
        list.add(Pair(context.getString(R.string.system_manufacture), Build.MANUFACTURER))
        list.add(Pair(context.getString(R.string.system_model), Build.MODEL))
        list.add(Pair(context.getString(R.string.system_brand), Build.BRAND))
        list.add(Pair(context.getString(R.string.system_release), Build.VERSION.RELEASE))
        list.add(Pair(context.getString(R.string.system_api), Build.VERSION.SDK_INT.toString()))
        list.add(Pair(context.getString(R.string.system_code_name), Build.VERSION.CODENAME))
        list.add(Pair(context.getString(R.string.system_device), Build.DEVICE))
        list.add(Pair(context.getString(R.string.system_product), Build.PRODUCT))
        list.add(Pair(context.getString(R.string.system_board), Build.BOARD))
        list.add(Pair(context.getString(R.string.system_platform), SocUtils.getSocInfo() ?: Constants.UNKNOWN))
        list.add(Pair(context.getString(R.string.system_build), Build.ID))
        list.add(Pair(context.getString(R.string.system_vm), System.getProperty("java.vm.version") ?: Constants.UNKNOWN))
        list.add(Pair(context.getString(R.string.system_security), CommandUtils.getProperty("ro.build.version.security_patch") ?: Constants.UNKNOWN))
        list.add(Pair(context.getString(R.string.system_baseband), CommandUtils.getProperty("gsm.version.baseband") ?: Constants.UNKNOWN))
        list.add(Pair(context.getString(R.string.system_build_type), Build.TYPE))
        list.add(Pair(context.getString(R.string.system_tags), Build.TAGS))
        list.add(Pair(context.getString(R.string.system_incremental), Build.VERSION.INCREMENTAL))
        list.add(Pair(context.getString(R.string.system_description), CommandUtils.getProperty("ro.build.description") ?: Constants.UNKNOWN))
        list.add(Pair(context.getString(R.string.system_fingerprint), Build.FINGERPRINT))
        val pm: PackageManager = context.packageManager
        val features: Array<FeatureInfo>? = pm.systemAvailableFeatures
        val featureCount = features?.size?.toString() ?: "0"
        list.add(Pair(context.getString(R.string.system_device_features), featureCount))
        list.add(Pair(context.getString(R.string.system_language), SystemConfigUtils.getCurrentLanguage(context)))
        list.add(Pair(context.getString(R.string.system_timezone), SystemConfigUtils.getCurrentTimeZone()))
        list.add(Pair(context.getString(R.string.system_uptime), SystemConfigUtils.getSystemUpdate()))
        list.add(Pair("displayId", Build.DISPLAY))
        list.add(Pair("bootloader", Build.BOOTLOADER))
        list.add(Pair("hardware", Build.HARDWARE))
        list.add(Pair("buildUser", Build.USER))
        list.add(Pair("buildHost", Build.HOST))
        list.add(Pair("bootId", FileUtils.readFile("/proc/sys/kernel/random/boot_id")))
        list.add(Pair("UUID", FileUtils.readFile("/proc/sys/kernel/random/uuid")))
        list.add(Pair("characteristics", CommandUtils.getProperty("ro.build.characteristics") ?: Constants.UNKNOWN))
        return list
    }
}
