package com.dofun.safe.deviceinfo.utils

import androidx.core.util.Pair
import com.google.gson.Gson
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object CommandUtils {
    fun getProperty(propName: String): String? {
        return try {
            val roSecureObj = Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .invoke(null, propName)
            roSecureObj as? String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun execute(command: String): String? {
        var bufferedOutputStream: BufferedOutputStream? = null
        var bufferedInputStream: BufferedInputStream? = null
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec("sh")
            bufferedOutputStream = BufferedOutputStream(process.outputStream)
            bufferedInputStream = BufferedInputStream(process.inputStream)
            bufferedOutputStream.write(command.toByteArray())
            bufferedOutputStream.write(10)
            bufferedOutputStream.flush()
            bufferedOutputStream.close()
            process.waitFor()
            val outputStr = getStrFromBufferInputSteam(bufferedInputStream)
            val result = if (outputStr.isNotEmpty() && outputStr.last() == '\n') {
                outputStr.substring(0, outputStr.length - 1)
            } else {
                outputStr
            }
            try {
                bufferedOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                bufferedInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process.destroy()
            return result
        } catch (e: Exception) {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            process?.destroy()
            return null
        } catch (throwable: Throwable) {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            process?.destroy()
            throw throwable
        }
    }

    private fun getStrFromBufferInputSteam(bufferedInputStream: BufferedInputStream?): String {
        if (bufferedInputStream == null) {
            return ""
        }
        val buffer = ByteArray(512)
        val result = StringBuilder()
        var read: Int
        do {
            read = try {
                bufferedInputStream.read(buffer)
            } catch (e: Exception) {
                e.printStackTrace()
                -1
            }
            if (read > 0) {
                result.append(String(buffer, 0, read))
            }
        } while (read >= 512)
        return result.toString()
    }

    fun convertToJson(batteryInfoList: List<Pair<String, String>>?): JSONObject {
        val `object` = JSONObject()
        if (batteryInfoList == null) {
            return `object`
        }
        for (pair in batteryInfoList) {
            if (pair != null) {
                val key = pair.first
                val value = pair.second
                if (key != null) {
                    try {
                        `object`.put(key, value)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return `object`
    }

    fun <T> convertToJsonArray(dataList: List<T>): JSONArray {
        val gson = Gson()
        val jsonArray = JSONArray()
        return try {
            val jsonString = gson.toJson(dataList)
            JSONArray(jsonString)
        } catch (e: JSONException) {
            e.printStackTrace()
            jsonArray
        }
    }

    fun readFile(name: String): String {
        return readFile(File(name))
    }

    fun readFile(file: File): String {
        var fileReader: FileReader? = null
        var reader: BufferedReader? = null
        try {
            val sb = StringBuffer()
            fileReader = FileReader(file)
            reader = BufferedReader(fileReader)
            while (true) {
                val line = reader.readLine() ?: break
                sb.append(line)
                sb.append('\n')
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1)
            }
            val result = sb.toString()
            try {
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fileReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            if (reader != null) {
                try {
                    reader.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            if (fileReader == null) {
                return ""
            }
            return try {
                fileReader.close()
                ""
            } catch (closeException: IOException) {
                closeException.printStackTrace()
                ""
            }
        } catch (throwable: Throwable) {
            if (reader != null) {
                try {
                    reader.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            throw throwable
        }
    }
}
