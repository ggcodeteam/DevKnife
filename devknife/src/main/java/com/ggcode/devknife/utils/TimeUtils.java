package com.ggcode.devknife.utils;

import android.support.annotation.MainThread;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: passin
 * @date: 2019/7/20 16:50
 * @desc:
 */
public class TimeUtils {

    private TimeUtils() {
        throw new AssertionError("u can't instantiate me...");
    }

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @MainThread
    public static String format(final long millis) {
        return format.format(new Date(millis));
    }
}
