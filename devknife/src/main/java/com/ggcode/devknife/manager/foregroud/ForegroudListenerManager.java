package com.ggcode.devknife.manager.foregroud;

import com.ggcode.devknife.manager.page.FloatPageManager;

/**
 * @author: zbb 33775
 * @date: 2019/5/15 14:46
 * @desc:
 */
public class ForegroudListenerManager {

    int startedActivityCounts;

    public static ForegroudListenerManager getInstance() {
        return ForegroudListenerManager.Holder.INSTANCE;
    }

    public void onActivityStarted() {
        if (startedActivityCounts == 0) {
            FloatPageManager.getInstance().notifyForeground();
        }
        startedActivityCounts++;
    }

    public void onActivityStopped() {
        startedActivityCounts--;
        if (startedActivityCounts == 0) {
            FloatPageManager.getInstance().notifyBackground();
        }
    }

    private static class Holder {

        private static ForegroudListenerManager INSTANCE = new ForegroudListenerManager();
    }
}
