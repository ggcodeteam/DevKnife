package com.ggcode.devknife.manager.page;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.utils.LogUtils;
import com.ggcode.devknife.utils.Preconditions;

public class FloatPageManager {

    private static final String TAG = "FloatPageManager";
    private WindowManager mWindowManager;
    private Context mContext;
    private Map<Object, BaseFloatPage> mBaseFloatPageMap = new ConcurrentHashMap<>();
    private Handler mHandler = Component.uiHandler();

    public static FloatPageManager getInstance() {
        return Holder.INSTANCE;
    }

    public void notifyBackground() {
        for (Entry<Object, BaseFloatPage> baseFloatPageEntry : mBaseFloatPageMap.entrySet()) {
            baseFloatPageEntry.getValue().onEnterBackground();
        }
    }

    public void notifyForeground() {
        for (Entry<Object, BaseFloatPage> baseFloatPageEntry : mBaseFloatPageMap.entrySet()) {
            baseFloatPageEntry.getValue().onEnterForeground();
        }
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void startIntent(@NonNull final PageIntent pageIntent) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Preconditions.checkNotNull(pageIntent);
                    Preconditions.checkNotNull(pageIntent.getTargetClass());
                    BaseFloatPage page = mBaseFloatPageMap.get(pageIntent.getTargetClass());
                    if (page != null) {
                        page.show();
                        return;
                    }
                    page = pageIntent.getTargetClass().newInstance();
                    page.setBundle(pageIntent.getBundle());
                    mBaseFloatPageMap.put(page.getTag(), page);
                    page.performCreate(mContext);
                    mWindowManager.addView(page.getRootView(), page.mLayoutParams);
                    page.getRootView().requestLayout();
                } catch (InstantiationException e) {
                    LogUtils.e(TAG, e.getMessage());
                } catch (IllegalAccessException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * tag 默认为相应 FloatPage 的 Class 对象。
     * 有跨模块需求的悬浮界面可重写 {@link BaseFloatPage#getTag()}去跨模块拿到对应的 FloatPage。
     */
    public void remove(Object tag) {
        if (tag == null) {
            return;
        }
        BaseFloatPage page = mBaseFloatPageMap.get(tag);
        if (page == null) {
            return;
        }
        mWindowManager.removeView(page.getRootView());
        page.performDestroy();
        mBaseFloatPageMap.remove(tag);
    }

    /**
     * tag 默认为相应 FloatPage 的 Class 对象。
     * 有跨模块需求的悬浮界面可重写 {@link BaseFloatPage#getTag()}去跨模块拿到对应的 FloatPage。
     */
    @Nullable
    public BaseFloatPage getFloatPage(Object tag) {
        if (tag == null) {
            return null;
        }
        return mBaseFloatPageMap.get(tag);
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    private static class Holder {

        private static FloatPageManager INSTANCE = new FloatPageManager();
    }
}
