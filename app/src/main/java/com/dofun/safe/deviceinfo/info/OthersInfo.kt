package com.dofun.safe.deviceinfo.info

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.webkit.WebSettings
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.R
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.Constants
import com.dofun.safe.deviceinfo.utils.DensityUtils
import com.dofun.safe.deviceinfo.utils.FileUtils
import com.dofun.safe.deviceinfo.utils.SdUtils
import java.lang.reflect.Method
import java.util.Locale
import org.json.JSONObject

object OthersInfo {
    @JvmStatic
    fun getOthersInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getOthersInfo(context))
    }

    @JvmStatic
    fun getOthersInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        list.add(Pair("UA", getDefaultUserAgent(context)))
        list.add(Pair("poolSize", FileUtils.readFile("/proc/sys/kernel/random/poolsize")))
        list.add(Pair("entropyAvail", FileUtils.readFile("/proc/sys/kernel/random/entropy_avail")))
        list.add(Pair("writeThreshold", FileUtils.readFile("/proc/sys/kernel/random/write_wakeup_threshold")))
        list.add(Pair("secs", FileUtils.readFile("/proc/sys/kernel/random/urandom_min_reseed_secs")))
        list.add(Pair("country", Locale.getDefault().language + "-" + Locale.getDefault().country))
        list.add(Pair(context.getString(R.string.system_refresh_rate), DensityUtils.getRefreshRate(context).toString() + "Hz"))
        list.add(Pair("dpi", DensityUtils.getDensityDpi(context).toString()))
        list.add(Pair("density", DensityUtils.getDensity(context).toString()))
        list.add(
            Pair(
                "width * height",
                DensityUtils.getScreenWidth(context).toString() + " X " + DensityUtils.getScreenHeight(context)
            )
        )
        list.add(
            Pair(
                "widthDp * heightDp",
                DensityUtils.getScreenWidthWithDp(context).toString() + " X " + DensityUtils.getScreenHeightWithDp(context)
            )
        )
        list.add(Pair("statusBarHeight", DensityUtils.getStatusBarHeight(context).toString()))
        list.add(Pair("navigationBarHeight", DensityUtils.getNavigationBarHeight(context).toString()))
        try {
            list.add(Pair("screenBrightness", Settings.System.getInt(context.contentResolver, "screen_brightness").toString()))
            list.add(
                Pair(
                    "screenBrightnessAuto",
                    (Settings.System.getInt(context.contentResolver, "screen_brightness_mode") == 1).toString()
                )
            )
            val autoRotate = Settings.System.getInt(context.contentResolver, "accelerometer_rotation") == 1
            list.add(Pair("screenOrientationAuto", autoRotate.toString()))
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        list.add(Pair("hideStatusBar", DensityUtils.hideStatusBar(context).toString()))
        list.add(Pair("hasNavigationBar", DensityUtils.hasNavigationBar(context).toString()))
        list.add(Pair("sdCardEnable", SdUtils.isMounted().toString()))
        return list
    }

    private fun getDefaultUserAgent(context: Context): String {
        var ua: String? = null
        try {
            ua = System.getProperty("http.agent")
            if (TextUtils.isEmpty(ua)) {
                val localMethod: Method = WebSettings::class.java.getDeclaredMethod("getDefaultUserAgent", Context::class.java)
                ua = localMethod.invoke(WebSettings::class.java, context) as String
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (TextUtils.isEmpty(ua)) Constants.UNKNOWN else ua ?: Constants.UNKNOWN
    }
}
