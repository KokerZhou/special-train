package com.dofun.safe.deviceinfo.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager

object DensityUtils {
    fun getDensityDpi(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.densityDpi
    }

    fun getDensity(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.density
    }

    fun getScreenWidth(context: Context): Int {
        val display = context.resources.displayMetrics
        return display.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val display = context.resources.displayMetrics
        return display.heightPixels
    }

    fun getScreenWidthWithDp(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        return ((displayMetrics.widthPixels / density) + 0.5f).toInt()
    }

    fun getScreenHeightWithDp(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        return ((displayMetrics.heightPixels / density) + 0.5f).toInt()
    }

    fun getRefreshRate(context: Context?): Int {
        val windowManager = context?.getSystemService("window") as? WindowManager ?: return 0
        val display: Display = if (Build.VERSION.SDK_INT >= 30) {
            context.display
        } else {
            windowManager.defaultDisplay
        }
        val refreshRate = display.refreshRate
        return refreshRate.toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun hideStatusBar(context: Context): Boolean {
        return checkFullScreenByTheme(context) || checkFullScreenByCode(context) || checkFullScreenByCode2(context)
    }

    private fun checkFullScreenByTheme(context: Context): Boolean {
        val theme = context.theme
        if (theme != null) {
            val typedValue = TypedValue()
            val result = theme.resolveAttribute(16843277, typedValue, false)
            if (result) {
                typedValue.coerceToString()
                return typedValue.type == 18 && typedValue.data != 0
            }
        }
        return false
    }

    private fun checkFullScreenByCode(context: Context): Boolean {
        val window: Window = (context as? Activity)?.window ?: return false
        val decorView: View = window.decorView
        return decorView.systemUiVisibility and 4 == 4
    }

    private fun checkFullScreenByCode2(context: Context): Boolean {
        val activity = context as? Activity ?: return false
        return activity.window.attributes.flags and 1024 == 1024
    }

    fun hasNavigationBar(context: Context): Boolean {
        val activity = context as? Activity ?: return false
        val windowManager = activity.windowManager
        val display = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        display.getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }
}
