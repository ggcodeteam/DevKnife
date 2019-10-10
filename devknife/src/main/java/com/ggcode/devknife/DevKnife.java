package com.ggcode.devknife;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.config.ColorPickerConfig;
import com.ggcode.devknife.config.FloatIconConfig;
import com.ggcode.devknife.manager.ActivityManager;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.manager.init.KnifeGroup;
import com.ggcode.devknife.manager.page.BaseFloatPage;
import com.ggcode.devknife.manager.page.FloatPageManager;
import com.ggcode.devknife.manager.page.PageIntent;
import com.ggcode.devknife.ui.home.DevToolsActivity;
import com.ggcode.devknife.ui.icon.FloatIconPage;
import com.ggcode.devknife.utils.LogUtils;
import com.ggcode.devknife.utils.Preconditions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/15 16:03
 * @desc:
 */
public class DevKnife {

    private static volatile boolean isInit = false;

    public static synchronized void install(@NonNull final Application app) {
        install(app, null);
    }

    public static synchronized void install(@NonNull final Application app, @Nullable Config config) {
        Preconditions.checkNotNull(app);
        if (isInit) {
            return;
        }
        isInit = true;
        init(app, config);
    }

    private static void init(final Application app, Config config) {
        InitProxy.init(app, config);
    }

    public static void openLog() {
        LogUtils.setDebug(true);
    }

    public static void addHiddenActivityClass(Class<? extends Activity> hiddenActivityClass) {
        DevActivityLifecycle.mHiddenActivityClassSet.add(hiddenActivityClass);
    }

    /**
     * 获取悬浮按钮
     */
    public static void getFloatIconPage(Object tag) {
        FloatPageManager.getInstance().getFloatPage(tag);
    }

    public static void showFloatIcon() {
        BaseFloatPage floatPage = FloatPageManager.getInstance().getFloatPage(FloatIconPage.class);
        if (floatPage == null) {
            FloatPageManager.getInstance().startIntent(new PageIntent(FloatIconPage.class));
        } else {
            floatPage.show();
        }
    }

    public static void hideFloatIcon() {
        BaseFloatPage floatPage = FloatPageManager.getInstance().getFloatPage(FloatIconPage.class);
        if (floatPage != null) {
            floatPage.hide();
        }
    }

    public static void show() {
        Activity topActivity = ActivityManager.getInstance().getTopActivity();
        if (topActivity == null) {
            DevToolsActivity.start(Component.app());
            return;
        } else {
            DevToolsActivity.start(topActivity);
            topActivity.overridePendingTransition(R.anim.dk_alpha_show, R.anim.dk_no_anim);
        }
    }

    public static void hide() {
        Activity activity = ActivityManager.getInstance().findActivity(DevToolsActivity.class);
        if (activity != null) {
            activity.finish();
        }
    }

    public static Config newConfig() {
        return new Config();
    }

    public static class Config {

        List<Knife> mKnives;
        List<KnifeGroup> mKnifeGroups;
        HashSet<Class<? extends Activity>> mHiddenActivityClass;
        FloatIconConfig mFloatIconConfig;
        ColorPickerConfig mColorPickerConfig;

        public Config knives(List<Knife> knifeList) {
            mKnives = knifeList;
            return this;
        }

        public Config addKnife(Knife knife) {
            if (mKnives == null) {
                mKnives = new ArrayList<>();
            }
            mKnives.add(knife);
            return this;
        }

        public Config knifeGroups(List<KnifeGroup> knifeGroupList) {
            mKnifeGroups = knifeGroupList;
            return this;
        }

        public Config addKnifeGroup(KnifeGroup knifeGroup) {
            if (mKnifeGroups == null) {
                mKnifeGroups = new ArrayList<>();
            }
            mKnifeGroups.add(knifeGroup);
            return this;
        }

        /**
         * 视图上下文请使用 application。
         * 默认触摸事件代理只支持点击和长按监听
         */
        public Config floatIconConfig(FloatIconConfig floatIconConfig) {
            mFloatIconConfig = floatIconConfig;
            return this;
        }

        public Config colorPickerConfig(ColorPickerConfig colorPickerConfig) {
            mColorPickerConfig = colorPickerConfig;
            return this;
        }

        /**
         * 在该界面会隐藏悬浮工具
         */
        public Config addHiddenActivity(Class<? extends Activity> hiddenActivityClass) {
            if (mHiddenActivityClass == null) {
                mHiddenActivityClass = new HashSet<>();
            }
            mHiddenActivityClass.add(hiddenActivityClass);
            return this;
        }
    }
}