package com.dofun.safe.deviceinfo.utils

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.text.format.Formatter
import com.dofun.safe.deviceinfo.bean.StorageBean
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Locale
import java.util.UUID

object SdUtils {
    private val units = arrayOf("B", "KB", "MB", "GB", "TB")

    fun isMounted(): Boolean {
        return Environment.getExternalStorageState() == "mounted"
    }

    fun getStoreInfo(context: Context, bean: StorageBean) {
        val card = Environment.getExternalStorageDirectory()
        bean.storePath = card.absolutePath
        val totalSpace = card.totalSpace
        val freeSpace = card.freeSpace
        val usableSpace = totalSpace - freeSpace
        val total = Formatter.formatFileSize(context, totalSpace)
        val usable = Formatter.formatFileSize(context, usableSpace)
        val free = Formatter.formatFileSize(context, freeSpace)
        bean.totalStore = total
        bean.freeStore = free
        bean.usedStore = usable
        val ratio = ((usableSpace / totalSpace.toDouble()) * 100.0).toInt()
        bean.ratioStore = ratio
        bean.romSize = getRealStorage(context)
    }

    fun getRealStorage(context: Context): String? {
        var totalSize = 0L
        return try {
            val storageManager = context.getSystemService("storage") as StorageManager
            val version = Build.VERSION.SDK_INT
            val unit = if (version >= 26) 1000.0f else 1024.0f
            if (version < 23) {
                val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
                val volumeList = getVolumeList.invoke(storageManager) as? Array<StorageVolume>
                if (volumeList != null) {
                    var getPathFile: Method? = null
                    for (volume in volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.javaClass.getDeclaredMethod("getPathFile")
                        }
                        val file = getPathFile.invoke(volume) as File
                        totalSize += file.totalSpace
                    }
                }
            } else {
                val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes")
                val getVolumeInfo = getVolumes.invoke(storageManager) as List<*>
                for (obj in getVolumeInfo) {
                    try {
                        val getType: Field = obj?.javaClass?.getField("type") ?: continue
                        val type = getType.getInt(obj)
                        if (type == 1) {
                            var size = 0L
                            if (version >= 26) {
                                val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                                val fsUuid = getFsUuid.invoke(obj) as String?
                                size = getTotalSize(context, fsUuid)
                            } else if (version >= 25) {
                                val getPrimaryStorageSize = StorageManager::class.java.getMethod("getPrimaryStorageSize")
                                size = getPrimaryStorageSize.invoke(storageManager) as Long
                            }
                            val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                            val readable = isMountedReadable.invoke(obj) as Boolean
                            if (readable) {
                                val fileMethod = obj.javaClass.getDeclaredMethod("getPath")
                                val file = fileMethod.invoke(obj) as File
                                if (size == 0L) {
                                    size = file.totalSpace
                                }
                                totalSize += size
                            }
                        } else if (type == 0) {
                            val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                            val readable = isMountedReadable.invoke(obj) as Boolean
                            if (readable) {
                                val fileMethod = obj.javaClass.getDeclaredMethod("getPath")
                                val file = fileMethod.invoke(obj) as File
                                totalSize += file.totalSpace
                            }
                        }
                    } catch (e: Exception) {
                        return null
                    }
                }
            }
            getUnit(totalSize.toFloat(), unit)
        } catch (e: Exception) {
            null
        }
    }

    private fun getUnit(sizeValue: Float, base: Float): String {
        var size = sizeValue
        var index = 0
        while (size > base && index < 4) {
            size /= base
            index++
        }
        return String.format(Locale.getDefault(), "%.2f %s ", size, units[index])
    }

    private fun getTotalSize(context: Context, fsUuid: String?): Long {
        val id = if (fsUuid == null) StorageManager.UUID_DEFAULT else UUID.fromString(fsUuid)
        return try {
            val stats = context.getSystemService(StorageStatsManager::class.java)
            stats.totalBytes(id)
        } catch (e: IOException) {
            e.printStackTrace()
            -1L
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            -1L
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            -1L
        } catch (e: NullPointerException) {
            e.printStackTrace()
            -1L
        }
    }
}
