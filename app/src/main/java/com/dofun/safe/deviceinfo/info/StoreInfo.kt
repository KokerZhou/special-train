package com.dofun.safe.deviceinfo.info

import android.content.Context
import com.dofun.safe.deviceinfo.bean.StorageBean
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.MemoryUtils
import com.dofun.safe.deviceinfo.utils.SdUtils
import org.json.JSONArray

object StoreInfo {
    @JvmStatic
    fun getStoreInfoJson(context: Context): JSONArray {
        return CommandUtils.convertToJsonArray(getStoreInfo(context))
    }

    @JvmStatic
    fun getStoreInfo(context: Context): List<StorageBean> {
        val list = ArrayList<StorageBean>()
        val bean = StorageBean()
        SdUtils.getStoreInfo(context, bean)
        MemoryUtils.getMemoryInfo(context, bean)
        list.add(bean)
        return list
    }
}
