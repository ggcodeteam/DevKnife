package com.ggcode.devknife.knife.performance.blockmonitor;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.R;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;

/**
 * @author: zbb 33775
 * @date: 2019/4/24 14:15
 * @desc:
 */
public class BlockMonitor extends Knife {

    @Override
    public int getCategory() {
        return Category.PERFORMANCE;
    }

    @Override
    public int getName() {
        return R.string.dk_performance_block_monitor;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_page_block_monitor;
    }

    @Override
    public void onClick(Context context) {

    }

    @Override
    public void onInit(Application application) {

    }
}
