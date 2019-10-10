package com.ggcode.devknife.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: zbb 33775
 * @date: 2019/5/29 15:25
 * @desc:
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    public void post(Runnable runnable) {
        Component.uiHandler().post(runnable);
    }
}
