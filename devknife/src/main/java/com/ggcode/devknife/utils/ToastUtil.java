package com.ggcode.devknife.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: zbb 33775
 * @date: 2019/5/17 14:29
 * @desc:
 */
public class ToastUtil {

    public static void show(final CharSequence text) {
        Component.uiHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Component.app(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void show(@StringRes final int stringRes) {
        Component.uiHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Component.app(), Component.app().getText(stringRes), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
