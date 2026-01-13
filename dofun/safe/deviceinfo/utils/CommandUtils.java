package com.dofun.safe.deviceinfo.utils;

import androidx.core.util.Pair;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class CommandUtils {
    public static String getProperty(String propName) {
        try {
            Object roSecureObj = Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, propName);
            if (roSecureObj != null) {
                return (String) roSecureObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String execute(String command) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            bufferedOutputStream = new BufferedOutputStream(process.getOutputStream());
            bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedOutputStream.write(command.getBytes());
            bufferedOutputStream.write(10);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            process.waitFor();
            String outputStr = getStrFromBufferInputSteam(bufferedInputStream);
            if (outputStr.charAt(outputStr.length() - 1) == '\n') {
                String substring = outputStr.substring(0, outputStr.length() - 1);
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedInputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (process != null) {
                    process.destroy();
                }
                return substring;
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            try {
                bufferedInputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
            return outputStr;
        } catch (Exception e5) {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            if (process == null) {
                return null;
            }
            process.destroy();
            return null;
        } catch (Throwable th) {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }

    private static String getStrFromBufferInputSteam(BufferedInputStream bufferedInputStream) {
        int read;
        if (bufferedInputStream == null) {
            return "";
        }
        byte[] buffer = new byte[512];
        StringBuilder result = new StringBuilder();
        do {
            try {
                read = bufferedInputStream.read(buffer);
                if (read > 0) {
                    result.append(new String(buffer, 0, read));
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (read >= 512);
        return result.toString();
    }

    public static JSONObject convertToJson(List<Pair<String, String>> batteryInfoList) {
        JSONObject object = new JSONObject();
        if (batteryInfoList == null) {
            return object;
        }
        for (Pair<String, String> pair : batteryInfoList) {
            if (pair != null) {
                String key = pair.first;
                String value = pair.second;
                if (key != null) {
                    try {
                        object.put(key, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return object;
    }

    public static <T> JSONArray convertToJsonArray(List<T> dataList) {
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONArray();
        try {
            String jsonString = gson.toJson(dataList);
            JSONArray jsonArray2 = new JSONArray(jsonString);
            return jsonArray2;
        } catch (JSONException e) {
            e.printStackTrace();
            return jsonArray;
        }
    }

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
                e3.printStackTrace();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (fileReader == null) {
                    return "";
                }
                try {
                    fileReader.close();
                    return "";
                } catch (IOException e5) {
                    e5.printStackTrace();
                    return "";
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
