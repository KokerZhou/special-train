package com.dofun.safe.deviceinfo.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.text.TextUtils
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.bean.CpuBean
import com.dofun.safe.deviceinfo.utils.Constants
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.HashSet

object SocUtils {
    fun getSocInfo(): String? {
        val socStr = CommandUtils.execute("getprop ro.board.platform")
        if (!TextUtils.isEmpty(socStr)) {
            return socStr
        }
        val socStr2 = CommandUtils.execute("getprop ro.hardware")
        return if (TextUtils.isEmpty(socStr2)) {
            CommandUtils.execute("getprop ro.boot.hardware")
        } else {
            socStr2
        }
    }

    fun setCpuInfo(list: MutableList<Pair<String, String>>) {
        try {
            val bufferedReader = BufferedReader(FileReader("/proc/cpuinfo"))
            val bean = CpuBean()
            val parts = HashSet<String>()
            val implementer = HashSet<String>()
            while (true) {
                val line = bufferedReader.readLine() ?: break
                val result = line.lowercase()
                FileLogger.d("soc-", "CPU: $result")
                val split = result.split(":\\s+".toRegex(), limit = 2)
                if (split[0].startsWith("cpu part")) {
                    parts.add(split[1])
                } else if (split[0].startsWith("hardware")) {
                    bean.hardware = split[1]
                } else if (split[0].startsWith("features")) {
                    bean.features = split[1]
                } else if (split[0].startsWith("cpu implementer")) {
                    implementer.add(split[1])
                }
            }
            bean.parts = parts.toTypedArray()
            bean.implementers = implementer.toTypedArray()
            list.add(Pair("parts", parts.toString()))
            list.add(Pair("implementer", implementer.toString()))
            list.add(Pair("hardware", bean.hardware ?: Constants.UNKNOWN))
            list.add(Pair("features", bean.features ?: Constants.UNKNOWN))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getGPUInfo(context: Context, list: MutableList<Pair<String, String>>) {
        val am = context.applicationContext.getSystemService("activity") as ActivityManager
        val info: ConfigurationInfo = am.deviceConfigurationInfo
        list.add(Pair("GlEsVersion", info.glEsVersion))
        list.add(Pair("reqGlEsVersion", info.reqGlEsVersion.toString()))
        list.add(Pair("reqInputFeatures", info.reqInputFeatures.toString()))
        list.add(Pair("reqKeyboardType", info.reqKeyboardType.toString()))
        list.add(Pair("reqNavigation", info.reqNavigation.toString()))
        list.add(Pair("reqTouchScreen", info.reqTouchScreen.toString()))
        list.add(Pair("describeContents", info.describeContents().toString()))
    }
}
