package com.ggcode.devknife.base.component;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.ggcode.devknife.utils.SPUtils;

/**
 * @author: zbb 33775
 * @date: 2019/4/27 10:56
 * @desc:
 */
public class Component {

    private static Application mApplication;
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());
    private static Gson mGson = new Gson();

    private static class Holder {

        private static final Component INSTANCE = new Component();
    }

    public static Component getInstance() {
        return Holder.INSTANCE;
    }

    public static void init(@NonNull Application application) {
        mApplication = application;
        SPUtils.init();
    }

    public static Application app() {
        return mApplication;
    }

    public static Handler uiHandler() {
        return mUiHandler;
    }

    public static Gson gson() {
        return mGson;
    }
}
