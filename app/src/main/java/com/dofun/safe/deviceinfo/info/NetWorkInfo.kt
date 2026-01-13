package com.dofun.safe.deviceinfo.info

import android.content.Context
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.NetWorkUtils
import org.json.JSONObject

object NetWorkInfo {
    @JvmStatic
    fun getNetWorkInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getNetWorkInfo(context))
    }

    @JvmStatic
    fun getNetWorkInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        getNetWorkStatus(context, list)
        return list
    }

    private fun getNetWorkStatus(context: Context, list: MutableList<Pair<String, String>>) {
        list.add(Pair("netAvailability", NetWorkUtils.isNetworkConnected(context).toString()))
        list.add(Pair("mobileAvailability", NetWorkUtils.isMobileEnabled(context).toString()))
        list.add(Pair("wifiAvailability", NetWorkUtils.isWifi(context).toString()))
        list.add(Pair("netType", NetWorkUtils.getNetWorkType(context)))
    }
}
