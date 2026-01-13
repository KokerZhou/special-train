package com.dofun.safe.deviceinfo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

object NetWorkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val manager = context.getSystemService("connectivity") as ConnectivityManager
        val networkInfo: NetworkInfo? = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable
    }

    fun isMobileEnabled(context: Context): Boolean {
        val cm = context.getSystemService("connectivity") as ConnectivityManager
        return try {
            val cmClass = Class.forName(cm.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            (method.invoke(cm) as Boolean)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isWifi(context: Context): Boolean {
        val manager = context.getSystemService("connectivity") as ConnectivityManager?
        val networkInfo = manager?.getNetworkInfo(1)
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    fun getNetWorkType(context: Context): String {
        if (!isNetworkConnected(context)) {
            return "NONE"
        }
        if (isWifi(context)) {
            return "WIFI"
        }
        val telephonyManager = context.getSystemService("phone") as TelephonyManager?
        if (telephonyManager != null) {
            when (telephonyManager.networkType) {
                1 -> return "GPRS"
                2 -> return "EDGE"
                3 -> return "UMTS"
                4 -> return "CDMA"
                5 -> return "EVDO_0"
                6 -> return "EVDO_A"
                7 -> return "1xRTT"
                8 -> return "HSDPA"
                9 -> return "HSUPA"
                10 -> return "HSPA"
                11 -> return "IDEN"
                12 -> return "EVDO_B"
                13 -> return "LTE"
                14 -> return "EHRPD"
                15 -> return "HSPAP"
                20 -> return "5G"
            }
        }
        return "NONE"
    }
}
