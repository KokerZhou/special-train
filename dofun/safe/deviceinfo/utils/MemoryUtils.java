package com.dofun.safe.deviceinfo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import com.dofun.safe.deviceinfo.bean.StorageBean;

/* loaded from: classes6.dex */
public class MemoryUtils {
    public static void getMemoryInfo(Context context, StorageBean bean) {
        String ram;
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService("activity");
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            manager.getMemoryInfo(info);
            long totalMem = info.totalMem;
            long availMem = info.availMem;
            long usedMem = totalMem - availMem;
            String total = Formatter.formatFileSize(context, totalMem);
            String usable = Formatter.formatFileSize(context, usedMem);
            String free = Formatter.formatFileSize(context, availMem);
            bean.setTotalMemory(total);
            bean.setFreeMemory(free);
            bean.setUsedMemory(usable);
            int ratio = (int) ((availMem / totalMem) * 100.0d);
            bean.setRatioMemory(ratio);
            double v = ((totalMem / 1024) / 1024) / 1024.0d;
            if (v <= 1.0d) {
                ram = "1 GB";
            } else if (v <= 2.0d) {
                ram = "2 GB";
            } else if (v <= 4.0d) {
                ram = "4 GB";
            } else if (v <= 6.0d) {
                ram = "6 GB";
            } else if (v <= 8.0d) {
                ram = "8 GB";
            } else if (v <= 12.0d) {
                ram = "12 GB";
            } else {
                ram = "16 GB";
            }
            bean.setMemInfo(ram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
