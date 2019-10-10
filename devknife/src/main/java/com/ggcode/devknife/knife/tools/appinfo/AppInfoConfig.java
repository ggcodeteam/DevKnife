package com.ggcode.devknife.knife.tools.appinfo;

import android.support.annotation.IntDef;
import com.ggcode.devknife.utils.SPUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: passin
 * @date: 2019/7/6 23:48
 * @desc:
 */
public class AppInfoConfig {

    public static final int LAYOUT_STRATEGY_GRID = 0;
    public static final int LAYOUT_STRATEGY_LINEAR = 1;

    private static final String KEY_APP_LIST_SHOW_STRATEGY = "key_app_list_show_strategy";

    private static final String KEY_IS_CONTAINS_SYSTEM_APP = "key_is_contains_system_app";

    public static int getLastShowStrategy() {
        return SPUtils.getInt(KEY_APP_LIST_SHOW_STRATEGY, LAYOUT_STRATEGY_GRID);
    }

    public static void saveIsContainsSystemApp(boolean isContains) {
        SPUtils.put(KEY_IS_CONTAINS_SYSTEM_APP, isContains);
    }

    public static boolean isContainsSystemApp() {
        return SPUtils.getBoolean(KEY_IS_CONTAINS_SYSTEM_APP, false);
    }

    public static void saveLastShowStrategy(@LayoutType int layoutType) {
        SPUtils.put(KEY_APP_LIST_SHOW_STRATEGY, layoutType);
    }

    @IntDef({LAYOUT_STRATEGY_GRID, LAYOUT_STRATEGY_LINEAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutType {

    }
}
