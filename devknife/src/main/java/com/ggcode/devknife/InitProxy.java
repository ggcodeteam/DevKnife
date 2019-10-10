package com.ggcode.devknife;

import android.app.Application;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.ggcode.devknife.DevKnife.Config;
import com.ggcode.devknife.knife.ui.colorpicker.ColorPickerInfoPage;
import com.ggcode.devknife.manager.init.KnifeInitManager;
import com.ggcode.devknife.manager.page.FloatPageManager;
import com.ggcode.devknife.ui.icon.FloatIconPage;
import com.ggcode.devknife.ui.load.LoadingCallback;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.utils.PermissionUtil;
import me.passin.loadknife.core.LoadKnife;

/**
 * @author: zbb 33775
 * @date: 2019/5/23 17:58
 * @desc:
 */
class InitProxy {

    private InitProxy() {
        throw new AssertionError("u can't instantiate me...");
    }

    static void init(Application app, Config config) {
        Component.init(app);
        if (config != null) {
            // knife 初始化和排序
            KnifeInitManager.init(config.mKnifeGroups, config.mKnives);
            // 不显示悬浮入口的界面
            if (config.mHiddenActivityClass != null) {
                DevActivityLifecycle.mHiddenActivityClassSet.addAll(config.mHiddenActivityClass);
            }
            initPageConfig(config);
        } else {
            KnifeInitManager.init();
        }

        // activitylifecycle
        app.registerActivityLifecycleCallbacks(new DevActivityLifecycle());

        // 请求悬浮窗权限
        PermissionUtil.requestDrawOverlays(app);
        FloatPageManager.getInstance().init(app);

        // 页面切换
        LoadKnife.newBuilder()
                .defaultCallback(LoadingCallback.class)
                .initializeDefault();
    }

    private static void initPageConfig(Config config) {
        if (config.mFloatIconConfig != null) {
            FloatIconPage.config(config.mFloatIconConfig);
        }
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            ColorPickerInfoPage.config(config.mColorPickerConfig);
        }
    }
}
