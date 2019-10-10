package com.ggcode.devknife.knife.tools.networklog;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.R;
import com.ggcode.devknife.constant.FragmentLabel;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.ui.base.ContainerActivity;

/**
 * @author: zbb 33775
 * @date: 2019/4/24 14:12
 * @desc:
 */
public class NetworkLog extends Knife {

    @Override
    public int getCategory() {
        return Category.TOOLS;
    }

    @Override
    public int getName() {
        return R.string.dk_tools_network_log;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_page_network_log;
    }

    @Override
    public void onClick(Context context) {
        ContainerActivity.start(context, FragmentLabel.NETWORK_LOG);
    }

    @Override
    public void onInit(Application application) {
    }

}
