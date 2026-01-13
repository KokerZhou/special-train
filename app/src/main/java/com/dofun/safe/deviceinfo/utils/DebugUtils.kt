package com.dofun.safe.deviceinfo.utils

import android.content.Context
import android.os.Debug
import android.provider.Settings

object DebugUtils {
    fun isOpenDebug(context: Context): Boolean {
        return try {
            Settings.Secure.getInt(context.contentResolver, "adb_enabled", 0) > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isDebugVersion(context: Context): Boolean {
        return try {
            context.applicationInfo.flags and 2 != 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isDebugConnected(): Boolean {
        return try {
            Debug.isDebuggerConnected()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getUsbDebugStatus(): String? {
        return CommandUtils.execute("getprop init.svc.adbd")
    }
}
