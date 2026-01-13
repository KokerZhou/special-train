package com.dofun.safe.deviceinfo.utils

import android.content.Context
import android.os.SystemClock
import java.util.Locale
import java.util.TimeZone

object SystemConfigUtils {
    fun getCurrentTimeZone(): String {
        val tz = TimeZone.getDefault()
        return tz.getDisplayName(false, 0)
    }

    fun getCurrentLanguage(context: Context): String {
        val locale = context.resources.configuration.locale
        val language = locale.language
        val country = locale.country
        val str = "${language}_${country}"
        return locale.displayLanguage
    }

    fun getSystemUpdate(): String {
        val nanoTime = SystemClock.elapsedRealtime()
        val day = nanoTime / 86400000
        return if (day > 0) {
            (nanoTime / 86400000).toString() + " days " + ((nanoTime % 86400000) / 3600000) + ":" +
                (((nanoTime % 86400000) % 3600000) / 60000)
        } else {
            ((nanoTime % 86400000) / 3600000).toString() + ":" + (((nanoTime % 86400000) % 3600000) / 60000)
        }
    }
}
