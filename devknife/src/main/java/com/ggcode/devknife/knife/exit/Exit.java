package com.ggcode.devknife.knife.exit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.R;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.utils.DKUtils;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 17:38
 * @desc:
 */
public class Exit extends Knife {

    @Override
    public int getCategory() {
        return Category.EXIT;
    }

    @Override
    public int getName() {
        return R.string.dk_category_exit;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public void onClick(Context context) {
        DKUtils.finish((Activity) context);
    }

    @Override
    public void onInit(Application application) {
    }
}
