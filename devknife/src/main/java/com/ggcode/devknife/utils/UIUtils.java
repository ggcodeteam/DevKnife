package com.ggcode.devknife.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.StringRes;
import android.view.WindowManager;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: zbb 33775
 * @date: 2019/5/17 13:49
 * @desc:
 */
public class UIUtils {

    private UIUtils() {
        throw new AssertionError("u can't instantiate me...");
    }

    public static int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(int pxValue) {
        final float densityDpi = getResources().getDisplayMetrics().density;
        return (int) (pxValue / densityDpi + 0.5f);
    }

    public static Resources getResources() {
        return Component.app().getResources();
    }

    public static String getString(@StringRes int stringID) {
        return getResources().getString(stringID);
    }

    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) Component.app().getSystemService(Context.WINDOW_SERVICE);
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
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
