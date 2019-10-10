package com.ggcode.devknife.manager;

import android.app.Activity;
import android.support.annotation.Nullable;
import java.util.Stack;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 17:58
 * @desc:
 */
public class ActivityManager {

    /**
     * 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     */
    private Stack<Activity> activityStack = new Stack<>();
    /**
     * 当前在前台的 Activity
     */
    private Activity mCurrentActivity;

    public static ActivityManager getInstance() {
        return ActivityManager.Holder.INSTANCE;
    }

    public synchronized void push(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activityStack.contains(activity)) {
            return;
        }
        activityStack.push(activity);
    }

    public synchronized boolean pop(Activity activity) {
        return activityStack.remove(activity);
    }

    public Activity findActivity(Class<?> activityClass) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(activityClass)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 获取在前台的 {@link Activity} (保证获取到的 {@link Activity} 正处于可见状态。
     */
    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获取最近启动的一个 {@link Activity}, 此方法不保证获取到的 {@link Activity} 正处于前台可见状态
     * 即使 App 进入后台或在这个 {@link Activity} 中打开一个之前已经存在的 {@link Activity}, 这时调用此方法
     * 还是会返回这个最近启动的 {@link Activity}, 因此基本不会出现 {@code null} 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     */
    @Nullable
    public Activity getTopActivity() {
        return activityStack.size() > 0 ? activityStack.get(activityStack.size() - 1) : null;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    private static class Holder {

        private static ActivityManager INSTANCE = new ActivityManager();
    }
}
