package com.ggcode.devknife.manager.init;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.google.gson.annotations.JsonAdapter;
import com.ggcode.devknife.ui.home.KnifeTypeAdapter;

/**
 * @author : zbb 33775
 * @date: 2019/3/18 11:20
 * @desc: 界面类
 */
@JsonAdapter(KnifeTypeAdapter.class)
public abstract class Knife implements Comparable<Knife> {

    private String tag;

    private int priority;

    public Knife() {
        tag = getClass().getCanonicalName();
    }

    public Knife(@NonNull String tag) {
        this.tag = tag;
    }

    abstract public int getCategory();

    @StringRes
    abstract public int getName();

    @DrawableRes
    abstract public int getIcon();

    abstract public void onClick(Context context);

    abstract public void onInit(Application application);

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int compareTo(Knife o) {
        return priority - o.getPriority();
    }

    /**
     * @return 是否拦截
     */
    public boolean onRequestPermissionsResult(Activity activity,int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        return false;
    }
}
