package com.ggcode.devknife.utils;

import android.app.Activity;
import com.ggcode.devknife.R;

/**
 * @author: passin
 * @date: 2019/7/4 23:04
 * @desc:
 */
public class DKUtils {

    public static void finish(Activity activity) {
        finishWithTransition(activity, R.anim.dk_no_anim, R.anim.dk_alpha_hide);
    }

    public static void finishWithTransition(Activity activity, int enterAnim, int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }
}
