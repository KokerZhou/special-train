package com.dofun.safe.devicedemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.dofun.safe.deviceinfo.SafeDeviceInfoManager
import com.dofun.safe.deviceinfo.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView: View = activityMainBinding.root
        setContentView(rootView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        SafeDeviceInfoManager.getInstance().init(this)
        activityMainBinding.bntStart.setOnClickListener(StartClickListener())
    }

    internal inner class StartClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            activityMainBinding.tvContent.text = "running..."
            Thread {
                val `object`: JSONObject = SafeDeviceInfoManager.getInstance().collectAllInfo(this@MainActivity)
                runOnUiThread {
                    LogUtils.printLongMsg(`object`.toString())
                    activityMainBinding.tvContent.text = formatJSONString(`object`.toString())
                }
            }.start()
        }
    }

    companion object {
        fun formatJSONString(s: String): String {
            val gson = GsonBuilder().setPrettyPrinting().create()
            return gson.toJson(gson.fromJson(s, Any::class.java))
        }
    }
}
