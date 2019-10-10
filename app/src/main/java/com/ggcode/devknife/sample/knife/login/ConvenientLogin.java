package com.ggcode.devknife.sample.knife.login;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.sample.R;

/**
 * @author: zbb 33775
 * @date: 2019/4/19 9:26
 * @desc:
 */
public class ConvenientLogin extends Knife {

    @Override
    public int getCategory() {
        return Category.TOOLS;
    }

    @Override
    public int getName() {
        return R.string.convenient_login;
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

