package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.DecimalUtils;
import com.dofun.safe.deviceinfo.utils.FileLogger;
import com.dofun.safe.deviceinfo.utils.FileUtils;
import com.dofun.safe.deviceinfo.utils.SocUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class SOCInfo {
    private static final FileFilter CPU_FILTER = new FileFilter() { // from class: com.dofun.safe.deviceinfo.info.SOCInfo.1
        @Override // java.io.FileFilter
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }
    };

    public static JSONObject getSOCInfoJson(Context context) {
        return CommandUtils.convertToJson(getSOCInfo(context));
    }

    public static List<Pair<String, String>> getSOCInfo(Context context) {
        List<Pair<String, String>> list = new ArrayList<>();
        SocUtils.setCpuInfo(list);
        setFrequency(list);
        list.add(new Pair<>("machine", CommandUtils.execute("uname -m")));
        list.add(new Pair<>("abi", Build.CPU_ABI));
        list.add(new Pair<>("CPU", CommandUtils.getProperty("ro.board.platform")));
        SocUtils.getGPUInfo(context, list);
        return list;
    }

    private static String getCpuTemp() {
        String temp = null;
        try {
            FileReader fr = new FileReader("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp");
            BufferedReader br = new BufferedReader(fr);
            temp = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(temp)) {
            return null;
        }
        return temp.length() >= 5 ? (Integer.parseInt(temp) / 1000) + "" : temp;
    }

    private static void setFrequency(List<Pair<String, String>> list) {
        try {
            int cores = ((File[]) Objects.requireNonNull(new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER))).length;
            list.add(new Pair<>("cores", cores + ""));
            if (cores > 0) {
                ArrayList<Integer> min = new ArrayList<>();
                ArrayList<Integer> max = new ArrayList<>();
                for (int i = 0; i < cores; i++) {
                    min.add(Integer.valueOf(Integer.parseInt(FileUtils.readFile(String.format("/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_min_freq", Integer.valueOf(i))))));
                    max.add(Integer.valueOf(Integer.parseInt(FileUtils.readFile(String.format("/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq", Integer.valueOf(i))))));
                }
                Collections.sort(min);
                Collections.sort(max);
                FileLogger.d("soc-", max.toString());
                if (max.size() > 0) {
                    list.add(new Pair<>("clockSpeed", min.get(0) + " - " + max.get(max.size() - 1) + " MHz"));
                    Map<Integer, Integer> map = new HashMap<>();
                    Iterator<Integer> it = max.iterator();
                    while (it.hasNext()) {
                        int temp = it.next().intValue();
                        Integer count = map.get(Integer.valueOf(temp));
                        map.put(Integer.valueOf(temp), Integer.valueOf(count == null ? 1 : count.intValue() + 1));
                    }
                    FileLogger.d("soc-", map.toString());
                    StringBuffer sb = new StringBuffer();
                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        sb.append(entry.getValue()).append(" x ").append(DecimalUtils.round((entry.getKey().intValue() / 1000.0d) / 1000.0d, 2)).append(" GHz").append('\n');
                    }
                    list.add(new Pair<>("clusters", sb.deleteCharAt(sb.length() - 1).toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
