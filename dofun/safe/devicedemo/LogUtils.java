package com.dofun.safe.devicedemo;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class LogUtils {
    private static final int LogUtilMaxLen = 3072;
    private static final String TAG = "LogUtils";
    private static ExecutorService executorService;
    private static String fileName;
    private static Process logcatProcess;
    private static DataOutputStream os;
    private static volatile boolean isRunning = false;
    private static int suPid = -1;

    public static void printLongMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.length() <= LogUtilMaxLen) {
                Log.d(TAG, msg);
                return;
            }
            String subStr = msg.substring(0, LogUtilMaxLen);
            Log.d(TAG, subStr);
            String nextStr = msg.substring(LogUtilMaxLen);
            printLongMsg(nextStr);
        }
    }

    public static void saveTaskLogcat(int taskId) {
        File logDir = new File("/storage/emulated/0/Android/data/com.dofun.safe.logintask/files/logs");
        if (logDir.exists() || logDir.mkdirs()) {
            fileName = "logcat_" + taskId + ".txt";
            final File logFile = new File(logDir, fileName);
            Log.d(TAG, "[Log] name = " + fileName);
            executorService = Executors.newSingleThreadExecutor();
            isRunning = true;
            executorService.execute(new Runnable() { // from class: com.dofun.safe.devicedemo.LogUtils$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LogUtils.lambda$saveTaskLogcat$0(logFile);
                }
            });
            return;
        }
        Log.e(TAG, "[Log] mkdir fail");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$saveTaskLogcat$0(File logFile) {
        try {
            ProcessBuilder pb = new ProcessBuilder("su");
            logcatProcess = pb.start();
            os = new DataOutputStream(logcatProcess.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
            os.writeBytes("echo $$\n");
            os.flush();
            String line = reader.readLine();
            if (line != null) {
                suPid = Integer.parseInt(line.trim());
                Log.d(TAG, "[Log] SU PID: " + suPid);
            }
            os.writeBytes("pkill -f logcat\n");
            os.flush();
            Thread.sleep(500L);
            os.writeBytes("logcat -c\n");
            os.writeBytes("logcat -v threadtime > " + logFile.getAbsolutePath() + "\n");
            os.flush();
            Log.d(TAG, "[Log] start");
            while (isRunning && logcatProcess != null) {
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    Log.d(TAG, "[Log] Process check failed: " + e.getMessage());
                    return;
                }
            }
        } catch (IOException e2) {
            Log.d(TAG, "[Log] error " + e2.getMessage());
            e2.printStackTrace();
        } catch (InterruptedException e3) {
            Log.d(TAG, "[Log] interrupted");
        }
    }

    public static void stopLogcat(final boolean delete) {
        if (isRunning) {
            Log.d(TAG, "[Log] stopping");
            isRunning = false;
            new Thread(new Runnable() { // from class: com.dofun.safe.devicedemo.LogUtils$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LogUtils.cleanup(delete);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cleanup(boolean delete) {
        try {
            if (os != null) {
                try {
                    os.writeBytes("pkill -f logcat\n");
                    Log.d(TAG, "[Log] cleanup pkill -f logcat");
                    os.flush();
                    Thread.sleep(500L);
                    os.writeBytes("exit\n");
                    Log.d(TAG, "[Log] cleanup exit");
                    os.flush();
                    Thread.sleep(500L);
                    os.close();
                } catch (IOException | InterruptedException e) {
                    Log.e(TAG, "[Log] cleanup Error closing output stream: " + e.getMessage());
                }
                os = null;
            }
            if (logcatProcess != null) {
                try {
                    if (!logcatProcess.waitFor(2L, TimeUnit.SECONDS)) {
                        Log.d(TAG, "[Log] cleanup logcatProcess.destroyForcibly()");
                        logcatProcess.destroyForcibly();
                        Log.d(TAG, "[Log] cleanup logcatProcess.waitFor(1, TimeUnit.SECONDS);");
                        logcatProcess.waitFor(1L, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e2) {
                    Log.e(TAG, "[Log] cleanup error waiting for process termination: " + e2.getMessage());
                }
                logcatProcess = null;
            }
            if (executorService != null) {
                try {
                    Log.d(TAG, "[Log] cleanup executorService.shutdownNow();");
                    executorService.shutdownNow();
                    Log.d(TAG, "[Log] cleanup executorService.awaitTermination(2, TimeUnit.SECONDS);");
                    executorService.awaitTermination(2L, TimeUnit.SECONDS);
                } catch (InterruptedException e3) {
                    Log.e(TAG, "[Log] cleanup shutting down executor error : " + e3.getMessage());
                }
                executorService = null;
            }
            try {
                Process checkProcess = Runtime.getRuntime().exec("su");
                DataOutputStream checkStream = new DataOutputStream(checkProcess.getOutputStream());
                Log.d(TAG, "[Log] pkill -f logcat");
                if (delete) {
                    checkStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.dofun.safe.logintask/files/logs/" + fileName + "\n");
                }
                checkStream.writeBytes("pkill -f logcat\n");
                checkStream.writeBytes("exit\n");
                checkStream.flush();
                checkStream.close();
                checkProcess.waitFor(1L, TimeUnit.SECONDS);
            } catch (Exception e4) {
                Log.e(TAG, "[Log] Final cleanup error: " + e4.getMessage());
            }
            suPid = -1;
            Log.d(TAG, "[Log] cleanup completed");
        } catch (Exception e5) {
            Log.e(TAG, "[Log] cleanup error: " + e5.getMessage());
            e5.printStackTrace();
        }
    }
}
