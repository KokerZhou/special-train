package com.dofun.safe.deviceinfo.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileLogger {
    private const val IS_DEBUG = true
    private const val LOG_FILE_NAME = "device.log"
    private const val TAG = "FileLogger"
    private var logFile: File? = null

    fun init(context: Context) {
        val dir = File(context.getExternalFilesDir(null), "logs")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        logFile = File(dir, LOG_FILE_NAME)
        if (logFile != null && logFile?.exists() == true) {
            val deleted = logFile?.delete() == true
            if (deleted) {
                Log.d(TAG, "Previous log file deleted")
            } else {
                Log.w(TAG, "Failed to delete previous log file")
            }
        }
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
        writeToFile("DEBUG", tag, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
        writeToFile("ERROR", tag, message)
    }

    private fun writeToFile(level: String, tag: String, message: String) {
        if (logFile == null) {
            Log.e(TAG, "Log file not initialized.")
            return
        }
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val logMessage = String.format("%s [%s/%s]: %s\n", timestamp, level, tag, message)
        try {
            val fos = FileOutputStream(logFile, true)
            fos.write(logMessage.toByteArray())
            fos.close()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write log to file.", e)
        }
    }
}
