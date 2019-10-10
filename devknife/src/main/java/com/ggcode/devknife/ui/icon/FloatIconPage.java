package com.ggcode.devknife.ui.icon;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.ggcode.devknife.DevKnife;
import com.ggcode.devknife.R;
import com.ggcode.devknife.config.FloatIconConfig;
import com.ggcode.devknife.manager.ActivityManager;
import com.ggcode.devknife.manager.page.BaseFloatPage;
import com.ggcode.devknife.ui.base.TouchProxy;
import com.ggcode.devknife.ui.base.TouchProxy.OnTouchEventListener;
import com.ggcode.devknife.utils.GGDialog;
import com.ggcode.devknife.utils.LogUtils;

/**
 * @author: zbb 33775
 * @date: 2019/5/15 12:15
 * @desc:
 */
public class FloatIconPage extends BaseFloatPage implements OnTouchEventListener {

    private static View mFloatIconView;
    private static boolean mUseDefualtTouchProxy;

    public static void config(FloatIconConfig floatIconConfig) {
        mFloatIconView = floatIconConfig.getFloatIconView();
        mUseDefualtTouchProxy = floatIconConfig.isUseDefualtTouchProxy();
    }

    @Override
    protected void onCreate() {
        if (mFloatIconView != null) {
            setContextView(mFloatIconView);
            if (mUseDefualtTouchProxy) {
                mFloatIconView.setOnTouchListener(new TouchProxy(FloatIconPage.this));
            }
        } else {
            setContextView(R.layout.dk_page_default_float_icon);
            getRootView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DevKnife.show();
                }
            });
            getRootView().setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showOftenUseInfoDialog();
                    return true;
                }
            });
            getRootView().setOnTouchListener(new TouchProxy(FloatIconPage.this));
        }
    }

    @Override
    protected void configWindowsLayoutParams(LayoutParams params) {
        params.x = FloatIconConfig.getLastPosX();
        params.y = FloatIconConfig.getLastPosY();

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    @Override
    public void onMove(float x, float y, float dx, float dy) {
        mLayoutParams.x += dx;
        mLayoutParams.y += dy;
        LogUtils.e("7777", mLayoutParams.x+"");
        updateViewLayout();
    }

    @Override
    public void onUp(float x, float y) {
        FloatIconConfig.saveLastPosXY(mLayoutParams.x, mLayoutParams.y);
    }

    @Override
    public void onDown(float x, float y) {
    }

    @Override
    public void onEnterBackground() {
        hide();
    }

    @Override
    public void onEnterForeground() {
        show();
    }

    private void showOftenUseInfoDialog() {
        GGDialog.with(ActivityManager.getInstance().getTopActivity())
                .animationId(R.style.DK_Animation_Alpha)
                .contentId(R.layout.dk_dialog_often_use_info)
                .build()
                .show();
    }
}
