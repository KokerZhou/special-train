package com.dofun.safe.deviceinfo.utils

import android.app.ActivityManager
import android.content.Context
import android.text.format.Formatter
import com.dofun.safe.deviceinfo.bean.StorageBean

object MemoryUtils {
    fun getMemoryInfo(context: Context, bean: StorageBean) {
        try {
            val manager = context.getSystemService("activity") as ActivityManager
            val info = ActivityManager.MemoryInfo()
            manager.getMemoryInfo(info)
            val totalMem = info.totalMem
            val availMem = info.availMem
            val usedMem = totalMem - availMem
            val total = Formatter.formatFileSize(context, totalMem)
            val usable = Formatter.formatFileSize(context, usedMem)
            val free = Formatter.formatFileSize(context, availMem)
            bean.totalMemory = total
            bean.freeMemory = free
            bean.usedMemory = usable
            val ratio = ((availMem / totalMem.toDouble()) * 100.0).toInt()
            bean.ratioMemory = ratio
            val v = (totalMem / 1024.0 / 1024.0 / 1024.0)
            val ram = when {
                v <= 1.0 -> "1 GB"
                v <= 2.0 -> "2 GB"
                v <= 4.0 -> "4 GB"
                v <= 6.0 -> "6 GB"
                v <= 8.0 -> "8 GB"
                v <= 12.0 -> "12 GB"
                else -> "16 GB"
            }
            bean.memInfo = ram
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
