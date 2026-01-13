package com.dofun.safe.deviceinfo.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: classes6.dex */
public class FileUtils {
    public static String readFile(String name) {
        return readFile(new File(name));
    }

    public static String readFile(File file) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            try {
                StringBuffer sb = new StringBuffer();
                fileReader = new FileReader(file);
                reader = new BufferedReader(fileReader);
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    sb.append('\n');
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                String stringBuffer = sb.toString();
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileReader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return stringBuffer;
            } catch (Exception e3) {
                Log.d("FileUtils", "readFile error.Just print stack trace.");
                e3.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (fileReader == null) {
                    return Constants.UNKNOWN;
                }
                try {
                    fileReader.close();
                    return Constants.UNKNOWN;
                } catch (IOException e5) {
                    e5.printStackTrace();
                    return Constants.UNKNOWN;
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            throw th;
        }
    }
}
