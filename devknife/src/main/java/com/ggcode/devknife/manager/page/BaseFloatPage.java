package com.ggcode.devknife.manager.page;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @author: zbb 33775
 * @date: 2019/5/5 14:03
 * @desc:
 */
public abstract class BaseFloatPage {

    protected WindowManager.LayoutParams mLayoutParams;
    private ViewGroup mRootView;
    private Context mContext;
    private Bundle mBundle;
    private View mContentView;
    private Handler mHandler;

    final void performCreate(Context context) {
        mHandler = new Handler(Looper.myLooper());
        mContext = context;
        mRootView = new FrameLayout(context) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    return onBackPressed();
                }
                return super.dispatchKeyEvent(event);
            }
        };

        onCreate();
        initWindowLayoutParams();
        configWindowsLayoutParams(mLayoutParams);
    }

    protected abstract void onCreate();

    private void initWindowLayoutParams() {
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        configWindowsLayoutParams(mLayoutParams);
    }

    protected void configWindowsLayoutParams(WindowManager.LayoutParams params) {
    }

    protected final void setContextView(@LayoutRes int id) {
        setContextView(LayoutInflater.from(mContext).inflate(id, mRootView, false));
    }

    protected final void setContextView(@NonNull View view) {
        mContentView = view;
        mRootView.removeAllViews();
        mRootView.addView(mContentView);
    }

    protected boolean onBackPressed() {
        return false;
    }

    final void performDestroy() {
        onDestroy();
        mBundle = null;
        mRootView = null;
        mContentView = null;
        mLayoutParams = null;
    }

    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    /**
     * @return applicationContext
     */
    public Context getContext() {
        return mContext;
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public final void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public void post(Runnable r) {
        mHandler.post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }

    public void finish() {
        FloatPageManager.getInstance().remove(getTag());
    }

    @NonNull
    public Object getTag() {
        return getClass();
    }

    public void show() {
        getRootView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getRootView().setVisibility(View.GONE);
    }

    protected ViewGroup getRootView() {
        return mRootView;
    }

    public WindowManager getWindowManager() {
        return FloatPageManager.getInstance().getWindowManager();
    }

    public void updateViewLayout() {
        getWindowManager().updateViewLayout(getRootView(), mLayoutParams);
    }

    public void onEnterBackground() {
    }

    public void onEnterForeground() {
    }
}
