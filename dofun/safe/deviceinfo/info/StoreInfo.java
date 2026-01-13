package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import com.dofun.safe.deviceinfo.bean.StorageBean;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.MemoryUtils;
import com.dofun.safe.deviceinfo.utils.SdUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/* loaded from: classes8.dex */
public class StoreInfo {
    public static JSONArray getStoreInfoJson(Context context) {
        return CommandUtils.convertToJsonArray(getStoreInfo(context));
    }

    public static List<StorageBean> getStoreInfo(Context context) {
        List<StorageBean> list = new ArrayList<>();
        StorageBean bean = new StorageBean();
        SdUtils.getStoreInfo(context, bean);
        MemoryUtils.getMemoryInfo(context, bean);
        list.add(bean);
        return list;
    }
}
