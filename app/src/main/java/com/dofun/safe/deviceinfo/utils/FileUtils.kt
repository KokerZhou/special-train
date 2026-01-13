package com.dofun.safe.deviceinfo.utils

import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object FileUtils {
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
            Log.d("FileUtils", "readFile error.Just print stack trace.")
            e.printStackTrace()
            if (reader != null) {
                try {
                    reader.close()
                } catch (closeException: IOException) {
                    closeException.printStackTrace()
                }
            }
            if (fileReader == null) {
                return Constants.UNKNOWN
            }
            return try {
                fileReader.close()
                Constants.UNKNOWN
            } catch (closeException: IOException) {
                closeException.printStackTrace()
                Constants.UNKNOWN
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
