package com.ggcode.devknife.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: passin
 * @date: 2019/7/21 21:23
 * @desc:
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError("u can't instantiate me...");
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) Component.app().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) Component.app().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static String getDensityType() {
        int densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        if (densityDpi <= DisplayMetrics.DENSITY_LOW ) {
            return "ldpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            return "mdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return "hdpi";
        }else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return "xhdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_XXHIGH) {
            return "xxhdpi";
        } else {
            return "xxxhdpi";
        }
    }
}
