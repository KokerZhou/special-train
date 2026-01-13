package com.dofun.safe.deviceinfo.info

import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.Constants
import com.dofun.safe.deviceinfo.utils.DecimalUtils
import com.dofun.safe.deviceinfo.utils.FileLogger
import com.dofun.safe.deviceinfo.utils.FileUtils
import com.dofun.safe.deviceinfo.utils.SocUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileReader
import java.io.IOException
import java.util.Collections
import java.util.HashMap
import java.util.regex.Pattern
import org.json.JSONObject

object SOCInfo {
    private val CPU_FILTER = FileFilter { pathname ->
        Pattern.matches("cpu[0-9]", pathname.name)
    }

    @JvmStatic
    fun getSOCInfoJson(context: Context): JSONObject {
        return CommandUtils.convertToJson(getSOCInfo(context))
    }

    @JvmStatic
    fun getSOCInfo(context: Context): List<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        SocUtils.setCpuInfo(list)
        setFrequency(list)
        list.add(Pair("machine", CommandUtils.execute("uname -m") ?: Constants.UNKNOWN))
        list.add(Pair("abi", Build.CPU_ABI))
        list.add(Pair("CPU", CommandUtils.getProperty("ro.board.platform") ?: Constants.UNKNOWN))
        SocUtils.getGPUInfo(context, list)
        return list
    }

    private fun getCpuTemp(): String? {
        var temp: String? = null
        try {
            val fr = FileReader("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp")
            val br = BufferedReader(fr)
            temp = br.readLine()
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (TextUtils.isEmpty(temp)) {
            return null
        }
        return if (temp!!.length >= 5) {
            (temp!!.toInt() / 1000).toString()
        } else {
            temp
        }
    }

    private fun setFrequency(list: MutableList<Pair<String, String>>) {
        try {
            val cores = File("/sys/devices/system/cpu/").listFiles(CPU_FILTER)?.size ?: 0
            list.add(Pair("cores", cores.toString()))
            if (cores > 0) {
                val min = ArrayList<Int>()
                val max = ArrayList<Int>()
                for (i in 0 until cores) {
                    min.add(
                        FileUtils.readFile(
                            String.format(
                                "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_min_freq",
                                i
                            )
                        ).toInt()
                    )
                    max.add(
                        FileUtils.readFile(
                            String.format(
                                "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq",
                                i
                            )
                        ).toInt()
                    )
                }
                Collections.sort(min)
                Collections.sort(max)
                FileLogger.d("soc-", max.toString())
                if (max.isNotEmpty()) {
                    list.add(Pair("clockSpeed", min[0].toString() + " - " + max[max.size - 1] + " MHz"))
                    val map = HashMap<Int, Int>()
                    for (temp in max) {
                        val count = map[temp]
                        map[temp] = (count ?: 0) + 1
                    }
                    FileLogger.d("soc-", map.toString())
                    val sb = StringBuffer()
                    for ((key, value) in map) {
                        sb.append(value)
                            .append(" x ")
                            .append(DecimalUtils.round((key / 1000.0) / 1000.0, 2))
                            .append(" GHz")
                            .append('\n')
                    }
                    list.add(Pair("clusters", sb.deleteCharAt(sb.length - 1).toString()))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
