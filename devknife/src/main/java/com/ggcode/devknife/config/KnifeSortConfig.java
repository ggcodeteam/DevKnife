package com.ggcode.devknife.config;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import com.ggcode.devknife.manager.init.KnifeGroup;
import com.ggcode.devknife.manager.init.KnifeInitManager;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.utils.SPUtils;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 17:57
 * @desc:
 */
public class KnifeSortConfig {

    private static final String KEY_KNIFE_SORT_JSON = "key_knife_sort_json";

    public static void saveKnifeSort() {
        String json = Component.gson().toJson(KnifeInitManager.getSortKnifeGroup());
        SPUtils.put(KEY_KNIFE_SORT_JSON, json, true);
    }

    public static List<KnifeGroup> getKnifeSort() {
        String json = SPUtils.getString(KEY_KNIFE_SORT_JSON);
        if (json.length() == 0) {
            return null;
        }
        try {
            return Component.gson().fromJson(json, new TypeToken<List<KnifeGroup>>() {
            }.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
