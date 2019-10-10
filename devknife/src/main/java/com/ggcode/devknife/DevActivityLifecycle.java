package com.ggcode.devknife;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.ggcode.devknife.manager.ActivityManager;
import com.ggcode.devknife.manager.foregroud.ForegroudListenerManager;
import com.ggcode.devknife.ui.home.DevToolsActivity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.ggcode.devknife.knife.tools.appinfo.info.AppInfoActivity;
import com.ggcode.devknife.knife.tools.appinfo.list.AppListActivity;
import com.ggcode.devknife.ui.base.TransparentActivity;

public class DevActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private ActivityManager mActivityManager = ActivityManager.getInstance();

    static final Set<Class<? extends Activity>> mHiddenActivityClassSet = Collections
            .synchronizedSet(new HashSet<Class<? extends Activity>>());

    static {
        mHiddenActivityClassSet.add(DevToolsActivity.class);
        mHiddenActivityClassSet.add(TransparentActivity.class);
        mHiddenActivityClassSet.add(AppInfoActivity.class);
        mHiddenActivityClassSet.add(AppListActivity.class);
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        mActivityManager.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ForegroudListenerManager.getInstance().onActivityStarted();
        // 显示隐藏悬浮按钮
        if (mHiddenActivityClassSet.contains(activity.getClass())) {
            DevKnife.hideFloatIcon();
        } else {
            DevKnife.showFloatIcon();
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        mActivityManager.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivityManager.setCurrentActivity(null);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ForegroudListenerManager.getInstance().onActivityStopped();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityManager.pop(activity);
    }
}
