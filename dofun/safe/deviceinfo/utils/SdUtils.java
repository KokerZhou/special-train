package com.dofun.safe.deviceinfo.utils;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
import com.dofun.safe.deviceinfo.bean.StorageBean;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes6.dex */
public class SdUtils {
    private static String[] units = {"B", "KB", "MB", "GB", "TB"};

    public static boolean isMounted() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static void getStoreInfo(Context context, StorageBean bean) {
        File card = Environment.getExternalStorageDirectory();
        bean.setStorePath(card.getAbsolutePath());
        long totalSpace = card.getTotalSpace();
        long freeSpace = card.getFreeSpace();
        long usableSpace = totalSpace - freeSpace;
        String total = Formatter.formatFileSize(context, totalSpace);
        String usable = Formatter.formatFileSize(context, usableSpace);
        String free = Formatter.formatFileSize(context, freeSpace);
        bean.setTotalStore(total);
        bean.setFreeStore(free);
        bean.setUsedStore(usable);
        int ratio = (int) ((usableSpace / totalSpace) * 100.0d);
        bean.setRatioStore(ratio);
        bean.setRomSize(getRealStorage(context));
    }

    public static String getRealStorage(Context context) {
        long total;
        long total2 = 0;
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            int version = Build.VERSION.SDK_INT;
            int i = 26;
            float unit = version >= 26 ? 1000.0f : 1024.0f;
            if (version < 23) {
                Method getVolumeList = StorageManager.class.getDeclaredMethod("getVolumeList", new Class[0]);
                StorageVolume[] volumeList = (StorageVolume[]) getVolumeList.invoke(storageManager, new Object[0]);
                if (volumeList != null) {
                    Method getPathFile = null;
                    for (StorageVolume volume : volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.getClass().getDeclaredMethod("getPathFile", new Class[0]);
                        }
                        File file = (File) getPathFile.invoke(volume, new Object[0]);
                        total2 += file.getTotalSpace();
                    }
                }
            } else {
                Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes", new Class[0]);
                List<Object> getVolumeInfo = (List) getVolumes.invoke(storageManager, new Object[0]);
                for (Object obj : getVolumeInfo) {
                    try {
                        Field getType = obj.getClass().getField("type");
                        int type = getType.getInt(obj);
                        if (type == 1) {
                            long totalSize = 0;
                            if (version >= i) {
                                total = total2;
                                try {
                                    Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid", new Class[0]);
                                    String fsUuid = (String) getFsUuid.invoke(obj, new Object[0]);
                                    totalSize = getTotalSize(context, fsUuid);
                                } catch (Exception e) {
                                    return null;
                                }
                            } else {
                                total = total2;
                                if (version >= 25) {
                                    Method getPrimaryStorageSize = StorageManager.class.getMethod("getPrimaryStorageSize", new Class[0]);
                                    totalSize = ((Long) getPrimaryStorageSize.invoke(storageManager, new Object[0])).longValue();
                                }
                            }
                            Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable", new Class[0]);
                            boolean readable = ((Boolean) isMountedReadable.invoke(obj, new Object[0])).booleanValue();
                            if (readable) {
                                Method file2 = obj.getClass().getDeclaredMethod("getPath", new Class[0]);
                                File f = (File) file2.invoke(obj, new Object[0]);
                                if (totalSize == 0) {
                                    totalSize = f.getTotalSpace();
                                }
                                total2 = total + totalSize;
                            } else {
                                total2 = total;
                            }
                        } else {
                            if (type == 0) {
                                Method isMountedReadable2 = obj.getClass().getDeclaredMethod("isMountedReadable", new Class[0]);
                                boolean readable2 = ((Boolean) isMountedReadable2.invoke(obj, new Object[0])).booleanValue();
                                if (readable2) {
                                    Method file3 = obj.getClass().getDeclaredMethod("getPath", new Class[0]);
                                    File f2 = (File) file3.invoke(obj, new Object[0]);
                                    total2 += f2.getTotalSpace();
                                }
                            }
                            total2 = total2;
                        }
                        i = 26;
                    } catch (Exception e2) {
                        return null;
                    }
                }
            }
            return getUnit((float) total2, unit);
        } catch (Exception e3) {
            return null;
        }
    }

    private static String getUnit(float size, float base) {
        int index = 0;
        while (size > base && index < 4) {
            size /= base;
            index++;
        }
        return String.format(Locale.getDefault(), "%.2f %s ", Float.valueOf(size), units[index]);
    }

    private static long getTotalSize(Context context, String fsUuid) {
        UUID id;
        try {
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = (StorageStatsManager) context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (IOException | NoClassDefFoundError | NoSuchFieldError | NullPointerException e) {
            e.printStackTrace();
            return -1L;
        }
    }
}
