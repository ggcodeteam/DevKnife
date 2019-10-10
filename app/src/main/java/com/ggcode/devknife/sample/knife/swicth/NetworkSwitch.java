package com.ggcode.devknife.sample.knife.swicth;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.sample.R;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 17:41
 * @desc:
 */
public class NetworkSwitch extends Knife {

    @Override
    public int getCategory() {
        return Category.TOOLS;
    }

    @Override
    public int getName() {
        return R.string.network_switch;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_network_switch;
    }

    @Override
    public void onClick(Context context) {

    }

    @Override
    public void onInit(Application application) {

    }
}
