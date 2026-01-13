package com.dofun.safe.devicedemo

import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object LogUtils {
    private const val LOG_UTIL_MAX_LEN = 3072
    private const val TAG = "LogUtils"
    private var executorService: ExecutorService? = null
    private var fileName: String? = null
    private var logcatProcess: Process? = null
    private var os: DataOutputStream? = null
    @Volatile
    private var isRunning = false
    private var suPid = -1

    fun printLongMsg(msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            val safeMsg = msg ?: return
            if (safeMsg.length <= LOG_UTIL_MAX_LEN) {
                Log.d(TAG, safeMsg)
                return
            }
            val subStr = safeMsg.substring(0, LOG_UTIL_MAX_LEN)
            Log.d(TAG, subStr)
            val nextStr = safeMsg.substring(LOG_UTIL_MAX_LEN)
            printLongMsg(nextStr)
        }
    }

    fun saveTaskLogcat(taskId: Int) {
        val logDir = File("/storage/emulated/0/Android/data/com.dofun.safe.logintask/files/logs")
        if (logDir.exists() || logDir.mkdirs()) {
            fileName = "logcat_${taskId}.txt"
            val logFile = File(logDir, fileName.orEmpty())
            Log.d(TAG, "[Log] name = ${fileName}")
            executorService = Executors.newSingleThreadExecutor()
            isRunning = true
            executorService?.execute {
                saveLogcatInternal(logFile)
            }
            return
        }
        Log.e(TAG, "[Log] mkdir fail")
    }

    private fun saveLogcatInternal(logFile: File) {
        try {
            val pb = ProcessBuilder("su")
            val process = pb.start()
            logcatProcess = process
            os = DataOutputStream(process.outputStream)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            os?.writeBytes("echo $$\n")
            os?.flush()
            val line = reader.readLine()
            if (line != null) {
                suPid = line.trim().toInt()
                Log.d(TAG, "[Log] SU PID: $suPid")
            }
            os?.writeBytes("pkill -f logcat\n")
            os?.flush()
            Thread.sleep(500L)
            os?.writeBytes("logcat -c\n")
            os?.writeBytes("logcat -v threadtime > ${logFile.absolutePath}\n")
            os?.flush()
            Log.d(TAG, "[Log] start")
            while (isRunning && logcatProcess != null) {
                try {
                    Thread.sleep(1000L)
                } catch (e: Exception) {
                    Log.d(TAG, "[Log] Process check failed: ${e.message}")
                    return
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, "[Log] error ${e.message}")
            e.printStackTrace()
        } catch (e: InterruptedException) {
            Log.d(TAG, "[Log] interrupted")
        }
    }

    fun stopLogcat(delete: Boolean) {
        if (isRunning) {
            Log.d(TAG, "[Log] stopping")
            isRunning = false
            Thread {
                cleanup(delete)
            }.start()
        }
    }

    private fun cleanup(delete: Boolean) {
        try {
            if (os != null) {
                try {
                    os?.writeBytes("pkill -f logcat\n")
                    Log.d(TAG, "[Log] cleanup pkill -f logcat")
                    os?.flush()
                    Thread.sleep(500L)
                    os?.writeBytes("exit\n")
                    Log.d(TAG, "[Log] cleanup exit")
                    os?.flush()
                    Thread.sleep(500L)
                    os?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "[Log] cleanup Error closing output stream: ${e.message}")
                } catch (e: InterruptedException) {
                    Log.e(TAG, "[Log] cleanup Error closing output stream: ${e.message}")
                }
                os = null
            }
            if (logcatProcess != null) {
                try {
                    if (logcatProcess?.waitFor(2L, TimeUnit.SECONDS) == false) {
                        Log.d(TAG, "[Log] cleanup logcatProcess.destroyForcibly()")
                        logcatProcess?.destroyForcibly()
                        Log.d(TAG, "[Log] cleanup logcatProcess.waitFor(1, TimeUnit.SECONDS);")
                        logcatProcess?.waitFor(1L, TimeUnit.SECONDS)
                    }
                } catch (e: InterruptedException) {
                    Log.e(TAG, "[Log] cleanup error waiting for process termination: ${e.message}")
                }
                logcatProcess = null
            }
            if (executorService != null) {
                try {
                    Log.d(TAG, "[Log] cleanup executorService.shutdownNow();")
                    executorService?.shutdownNow()
                    Log.d(TAG, "[Log] cleanup executorService.awaitTermination(2, TimeUnit.SECONDS);")
                    executorService?.awaitTermination(2L, TimeUnit.SECONDS)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "[Log] cleanup shutting down executor error : ${e.message}")
                }
                executorService = null
            }
            try {
                val checkProcess = Runtime.getRuntime().exec("su")
                val checkStream = DataOutputStream(checkProcess.outputStream)
                Log.d(TAG, "[Log] pkill -f logcat")
                if (delete) {
                    checkStream.writeBytes(
                        "rm -rf /storage/emulated/0/Android/data/com.dofun.safe.logintask/files/logs/${fileName}\n"
                    )
                }
                checkStream.writeBytes("pkill -f logcat\n")
                checkStream.writeBytes("exit\n")
                checkStream.flush()
                checkStream.close()
                checkProcess.waitFor(1L, TimeUnit.SECONDS)
            } catch (e: Exception) {
                Log.e(TAG, "[Log] Final cleanup error: ${e.message}")
            }
            suPid = -1
            Log.d(TAG, "[Log] cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "[Log] cleanup error: ${e.message}")
            e.printStackTrace()
        }
    }
}
