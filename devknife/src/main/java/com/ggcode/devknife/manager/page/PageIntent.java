package com.ggcode.devknife.manager.page;

import android.os.Bundle;
import android.support.annotation.NonNull;

public class PageIntent {

    private Class<? extends BaseFloatPage> targetClass;

    private Bundle bundle;

    private PageIntent() {
    }

    public PageIntent(@NonNull Class<? extends BaseFloatPage> targetClass) {
        this.targetClass = targetClass;
    }

    public void putBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Class<? extends BaseFloatPage> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<? extends BaseFloatPage> targetClass) {
        this.targetClass = targetClass;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
