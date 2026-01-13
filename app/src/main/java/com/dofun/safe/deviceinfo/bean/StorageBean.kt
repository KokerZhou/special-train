package com.dofun.safe.deviceinfo.bean

import org.json.JSONObject

class StorageBean {
    var freeMemory: String? = null
    var freeStore: String? = null
    var memInfo: String? = null
    var ratioMemory: Int = 0
    var ratioStore: Int = 0
    var romSize: String? = null
    var storePath: String? = null
    var totalMemory: String? = null
    var totalStore: String? = null
    var usedMemory: String? = null
    var usedStore: String? = null

    override fun toString(): String {
        return "StorageBean{freeStore='${freeStore}', usedStore='${usedStore}', totalStore='${totalStore}', ratioStore=${ratioStore}, storePath='${storePath}', freeMemory='${freeMemory}', usedMemory='${usedMemory}', totalMemory='${totalMemory}', ratioMemory=${ratioMemory}, memInfo='${memInfo}', romSize='${romSize}'}"
    }

    fun toJSON(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("freeStore=", freeStore)
            jsonObject.put("usedStore", usedStore)
            jsonObject.put("totalStore", totalStore)
            jsonObject.put("ratioStore", ratioStore)
            jsonObject.put("storePath", storePath)
            jsonObject.put("freeMemory", freeMemory)
            jsonObject.put("usedMemory", usedMemory)
            jsonObject.put("totalMemory", totalMemory)
            jsonObject.put("ratioMemory", ratioMemory)
            jsonObject.put("memInfo", memInfo)
            jsonObject.put("romSize", romSize)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonObject
    }
}
