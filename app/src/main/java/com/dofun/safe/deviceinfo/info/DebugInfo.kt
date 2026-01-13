package com.dofun.safe.deviceinfo.info

import android.content.Context
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.Constants
import com.dofun.safe.deviceinfo.utils.DebugUtils
import org.json.JSONObject

object DebugInfo {
    @JvmStatic
    fun getDebugInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getDebugInfo(context))
    }

    @JvmStatic
    fun getDebugInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        list.add(Pair("debugOpen", DebugUtils.isOpenDebug(context).toString()))
        list.add(Pair("usbDebugStatus", DebugUtils.getUsbDebugStatus() ?: Constants.UNKNOWN))
        list.add(Pair("debugVersion", DebugUtils.isDebugVersion(context).toString()))
        list.add(Pair("debugConnected", DebugUtils.isDebugConnected().toString()))
        return list
    }
}
