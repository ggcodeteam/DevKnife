package com.ggcode.devknife.manager.init;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import com.ggcode.devknife.R;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.config.KnifeSortConfig;
import com.ggcode.devknife.knife.exit.Exit;
import com.ggcode.devknife.knife.performance.blockmonitor.BlockMonitor;
import com.ggcode.devknife.knife.tools.appinfo.AppInfo;
import com.ggcode.devknife.knife.tools.networklog.NetworkLog;
import com.ggcode.devknife.knife.ui.colorpicker.ColorPicker;
import com.ggcode.devknife.utils.Preconditions;
import com.ggcode.devknife.utils.UIUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zbb 33775
 * @date: 2019/5/15 10:32
 * @desc:
 */
public class KnifeInitManager {

    private static Map<String, Knife> knifeMap = new HashMap<>();
    private static SparseArray<KnifeGroup> knifeGroupSparseArray = new SparseArray<>();
    private static List<KnifeGroup> mSortKnifeGroup = new ArrayList<>();

    public static void init() {
        init(null, null);
    }

    public static void init(@Nullable List<KnifeGroup> extraKnifeGroups, @Nullable List<Knife> extraKnives) {
        // 获取所有 KnifeGroup。
        List<KnifeGroup> knifeGroups = getAllKnifeGroup(extraKnifeGroups);

        initAllKnife(extraKnives);

        // 排序
        List<KnifeGroup> sortknifeGroups = KnifeSortConfig.getKnifeSort();
        if (sortknifeGroups != null) {
            sortKnifeAndGroup(sortknifeGroups);
        } else {
            mSortKnifeGroup = knifeGroups;
        }

        // 初始化所有 Knife。
        for (KnifeGroup knifeGroup : mSortKnifeGroup) {
            List<Knife> knives = knifeGroup.getKnives();
            for (Knife knife : knives) {
                knife.onInit(Component.app());
            }
        }
    }

    private static List<KnifeGroup> getAllKnifeGroup(List<KnifeGroup> extraKnifeGroups) {
        List<KnifeGroup> knifeGroups = new ArrayList<>();
        knifeGroups.add(new KnifeGroup(Category.TOOLS, UIUtils.getString(R.string.dk_category_tools)));
        knifeGroups.add(new KnifeGroup(Category.PERFORMANCE, UIUtils.getString(R.string.dk_category_performance)));
        knifeGroups.add(new KnifeGroup(Category.UI, UIUtils.getString(R.string.dk_category_ui)));
        if (extraKnifeGroups != null) {
            knifeGroups.addAll(extraKnifeGroups);
        }
        knifeGroups.add(new KnifeGroup(Category.EXIT, UIUtils.getString(R.string.dk_category_exit)));

        for (KnifeGroup knifeGroup : knifeGroups) {
            knifeGroupSparseArray.put(knifeGroup.getCategory(), knifeGroup);
        }
        return knifeGroups;
    }

    private static void initAllKnife(List<Knife> extraKnives) {
        List<Knife> allKnives = new ArrayList<>();

        // tool
        allKnives.add(new NetworkLog());
        // performance
        allKnives.add(new BlockMonitor());
        // ui
        allKnives.add(new ColorPicker());
        // other
        allKnives.add(new AppInfo());
        // edit
        allKnives.add(new Exit());

        if (extraKnives != null) {
            allKnives.addAll(extraKnives);
        }

        // 为 knife 打上 tag，并归组 KnifeGroup。
        for (Knife knife : allKnives) {
            knifeMap.put(knife.getTag(), knife);
            KnifeGroup knifeGroup = knifeGroupSparseArray.get(knife.getCategory());
            Preconditions.checkNotNull(knifeGroup, "There is no Category of " + knife.getCategory());
            knifeGroup.addKnife(knife);
        }
    }

    private static void sortKnifeAndGroup(List<KnifeGroup> lastSortknifeGroups) {
        int size = lastSortknifeGroups.size();
        for (int i = 0; i < size; i++) {
            // 获取上次正确排序的 KnifeGroup。
            KnifeGroup sortknifeGroup = lastSortknifeGroups.get(i);

            // 获取该次编译的 KnifeGroup。
            KnifeGroup knifeGroup = knifeGroupSparseArray.get(sortknifeGroup.getCategory());
            if (knifeGroup != null) {
                List<Knife> sortKnive = sortknifeGroup.getKnives();

                // 对 knife 设置排序编号
                for (Knife knifeSort : sortKnive) {
                    Knife knife = knifeMap.get(knifeSort.getTag());
                    if (knife != null) {
                        knife.setPriority(knifeSort.getPriority());
                    }
                }

                // 对 knife 排序
                Collections.sort(knifeGroup.getKnives());

                // 添加到该次排序中
                mSortKnifeGroup.add(knifeGroup);
                knifeGroup.setSort(true);
            }
        }

        // 添加新增的 KnifeGroup
        int knifeGroupSize = knifeGroupSparseArray.size();
        for (int i = 0; i < knifeGroupSize; i++) {
            KnifeGroup knifeGroup = knifeGroupSparseArray.valueAt(i);
            if (!knifeGroup.isSort()) {
                mSortKnifeGroup.add(knifeGroup);
            }
        }
        KnifeSortConfig.saveKnifeSort();
    }

    /**
     * 受需求和代码规范所致，设置为 public
     * 外部请不要修改该集合的内容，否则会崩溃。
     *
     * @hide
     */
    public static List<KnifeGroup> getSortKnifeGroup() {
        return mSortKnifeGroup;
    }
}
