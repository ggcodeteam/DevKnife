package com.ggcode.devknife.config;

import android.content.SharedPreferences.Editor;
import android.view.View;
import com.ggcode.devknife.utils.SPUtils;

/**
 * @author: zbb 33775
 * @date: 2019/5/16 13:54
 * @desc:
 */
public class FloatIconConfig {

    private static final String KEY_FLOAT_ICON_POS_X = "key_float_icon_pos_x";
    private static final String KEY_FLOAT_ICON_POS_Y = "key_float_icon_pos_y";
    private View mFloatIconView;
    private boolean mUseDefualtTouchProxy;

    public FloatIconConfig(View floatIconView, boolean useDefualtTouchProxy) {
        mFloatIconView = floatIconView;
        mUseDefualtTouchProxy = useDefualtTouchProxy;
    }

    public static int getLastPosX() {
        return SPUtils.getInt(KEY_FLOAT_ICON_POS_X, 0);
    }

    public static int getLastPosY() {
        return SPUtils.getInt(KEY_FLOAT_ICON_POS_Y, 0);
    }

    public static void saveLastPosXY(int x, int y) {
        Editor edit = SPUtils.getSp().edit();
        edit.putInt(KEY_FLOAT_ICON_POS_X, x);
        edit.putInt(KEY_FLOAT_ICON_POS_Y, y);
        edit.apply();
    }

    public View getFloatIconView() {
        return mFloatIconView;
    }

    public boolean isUseDefualtTouchProxy() {
        return mUseDefualtTouchProxy;
    }
}
