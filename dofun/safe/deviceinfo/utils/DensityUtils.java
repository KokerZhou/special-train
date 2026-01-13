package com.dofun.safe.deviceinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/* loaded from: classes6.dex */
public class DensityUtils {
    public static int getDensityDpi(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.densityDpi;
    }

    public static float getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.heightPixels;
    }

    public static int getScreenWidthWithDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return (int) ((displayMetrics.widthPixels / density) + 0.5f);
    }

    public static int getScreenHeightWithDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return (int) ((displayMetrics.heightPixels / density) + 0.5f);
    }

    public static int getRefreshRate(Context context) {
        WindowManager windowManager;
        Display display;
        if (context == null || (windowManager = (WindowManager) context.getSystemService("window")) == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            display = context.getDisplay();
        } else {
            display = windowManager.getDefaultDisplay();
        }
        float refreshRate = display.getRefreshRate();
        return (int) refreshRate;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static boolean hideStatusBar(Context context) {
        return checkFullScreenByTheme(context) || checkFullScreenByCode(context) || checkFullScreenByCode2(context);
    }

    private static boolean checkFullScreenByTheme(Context context) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedValue typedValue = new TypedValue();
            boolean result = theme.resolveAttribute(16843277, typedValue, false);
            if (result) {
                typedValue.coerceToString();
                return typedValue.type == 18 && typedValue.data != 0;
            }
        }
        return false;
    }

    private static boolean checkFullScreenByCode(Context context) {
        Window window;
        if (!(context instanceof Activity) || (window = ((Activity) context).getWindow()) == null) {
            return false;
        }
        View decorView = window.getDecorView();
        return (decorView.getSystemUiVisibility() & 4) == 4;
    }

    private static boolean checkFullScreenByCode2(Context context) {
        return (context instanceof Activity) && (((Activity) context).getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static boolean hasNavigationBar(Context context) {
        if (!(context instanceof Activity)) {
            return false;
        }
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0;
    }
}
