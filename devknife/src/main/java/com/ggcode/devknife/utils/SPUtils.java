package com.ggcode.devknife.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import java.util.Collections;
import java.util.Set;
import com.ggcode.devknife.base.component.Component;

public class SPUtils {

    private static final String SP_NAME = "DK_SP";
    private static SharedPreferences sp;

    public static void init() {
        if (sp == null) {
            sp = Component.app().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    public static String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public static String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit();
        } else {
            sp.edit().putInt(key, value).apply();
        }
    }

    public static int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    public static int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value).commit();
        } else {
            sp.edit().putLong(key, value).apply();
        }
    }

    public static long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    public static long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value).commit();
        } else {
            sp.edit().putFloat(key, value).apply();
        }
    }

    public static float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    public static float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public static void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit();
        } else {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    public static boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static void put(@NonNull final String key, final Set<String> value) {
        put(key, value, false);
    }

    public static void put(@NonNull final String key, final Set<String> value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value).commit();
        } else {
            sp.edit().putStringSet(key, value).apply();
        }
    }

    public static Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    public static Set<String> getStringSet(@NonNull final String key, final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    public static boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    public static void remove(@NonNull final String key) {
        remove(key, false);
    }

    public static void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key).commit();
        } else {
            sp.edit().remove(key).apply();
        }
    }

    public static void clear() {
        clear(false);
    }

    public static void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear().commit();
        } else {
            sp.edit().clear().apply();
        }
    }

    public static SharedPreferences getSp() {
        return sp;
    }
}
