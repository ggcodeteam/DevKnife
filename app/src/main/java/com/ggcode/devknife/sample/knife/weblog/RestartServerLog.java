package com.ggcode.devknife.sample.knife.weblog;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.sample.Category;
import com.ggcode.devknife.sample.R;

/**
 * @author: zbb 33775
 * @date: 2019/4/27 19:05
 * @desc:
 */
public class RestartServerLog extends Knife {

    @Override
    public int getCategory() {
        return Category.CUSTOM;
    }

    @Override
    public int getName() {
        return R.string.test;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public void onClick(Context context) {

    }

    @Override
    public void onInit(Application application) {

    }
}
