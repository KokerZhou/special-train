package com.dofun.safe.deviceinfo.utils;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes6.dex */
public class FileLogger {
    private static final boolean IS_DEBUG = true;
    private static final String LOG_FILE_NAME = "device.log";
    private static final String TAG = "FileLogger";
    private static File logFile;

    public static void init(Context context) {
        File dir = new File(context.getExternalFilesDir(null), "logs");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logFile = new File(dir, LOG_FILE_NAME);
        if (logFile != null && logFile.exists()) {
            boolean deleted = logFile.delete();
            if (deleted) {
                Log.d(TAG, "Previous log file deleted");
            } else {
                Log.w(TAG, "Failed to delete previous log file");
            }
        }
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
        writeToFile("DEBUG", tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
        writeToFile("ERROR", tag, message);
    }

    private static void writeToFile(String level, String tag, String message) {
        if (logFile == null) {
            Log.e(TAG, "Log file not initialized.");
            return;
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());
        String logMessage = String.format("%s [%s/%s]: %s\n", timestamp, level, tag, message);
        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write(logMessage.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to write log to file.", e);
        }
    }
}
