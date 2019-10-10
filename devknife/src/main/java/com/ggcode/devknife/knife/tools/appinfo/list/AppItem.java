package com.ggcode.devknife.knife.tools.appinfo.list;

import android.graphics.drawable.Drawable;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 14:59
 * @desc:
 */
public class AppItem {

    private String name;
    private String packageName;
    private Drawable icon;

    public AppItem(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

}
