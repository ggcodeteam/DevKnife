package com.ggcode.devknife.sample;

import android.app.Application;
import com.ggcode.devknife.DevKnife;
import com.ggcode.devknife.DevKnife.Config;
import com.ggcode.devknife.manager.init.KnifeGroup;
import com.ggcode.devknife.sample.knife.login.ConvenientLogin;
import com.ggcode.devknife.sample.knife.swicth.NetworkSwitch;
import com.ggcode.devknife.sample.knife.weblog.RestartServerLog;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 15:59
 * @desc:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config devKnifeConfig = DevKnife.newConfig()
                .addKnife(new NetworkSwitch())
                .addKnife(new ConvenientLogin())
                .addKnife(new RestartServerLog())
                .addKnifeGroup(new KnifeGroup(Category.CUSTOM, "测试"));

        DevKnife.install(this, devKnifeConfig);
        DevKnife.openLog();
    }
}
