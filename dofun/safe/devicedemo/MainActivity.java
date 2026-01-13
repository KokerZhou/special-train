package com.dofun.safe.devicedemo;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import com.dofun.safe.devicedemo.databinding.ActivityMainBinding;
import com.dofun.safe.deviceinfo.SafeDeviceInfoManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = this.activityMainBinding.getRoot();
        setContentView(rootView);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        SafeDeviceInfoManager.getInstance().init(this);
        this.activityMainBinding.bntStart.setOnClickListener(new AnonymousClass1());
    }

    /* renamed from: com.dofun.safe.devicedemo.MainActivity$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            MainActivity.this.activityMainBinding.tvContent.setText("running...");
            new Thread(new Runnable() { // from class: com.dofun.safe.devicedemo.MainActivity.1.1
                @Override // java.lang.Runnable
                public void run() {
                    final JSONObject object = SafeDeviceInfoManager.getInstance().collectAllInfo(MainActivity.this);
                    MainActivity.this.runOnUiThread(new Runnable() { // from class: com.dofun.safe.devicedemo.MainActivity.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            LogUtils.printLongMsg(object.toString());
                            MainActivity.this.activityMainBinding.tvContent.setText(MainActivity.formatJSONString(object.toString()));
                        }
                    });
                }
            }).start();
        }
    }

    public static String formatJSONString(String s) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(gson.fromJson(s, (Class<Object>) Object.class));
        return prettyJson;
    }
}
