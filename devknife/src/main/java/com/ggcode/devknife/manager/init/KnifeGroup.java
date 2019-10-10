package com.ggcode.devknife.manager.init;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/24 17:42
 * @desc:
 */
public class KnifeGroup {

    private String name;
    private int category;
    private List<Knife> knives;
    @Expose
    private boolean isSort;

    public KnifeGroup(int category, String name) {
        this(category, name, new ArrayList<Knife>());
    }

    public KnifeGroup(int category, String name, List<Knife> knives) {
        this.category = category;
        this.name = name;
        this.knives = knives;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<Knife> getKnives() {
        return knives;
    }

    public void setKnives(List<Knife> knives) {
        this.knives = knives;
    }

    public void addKnife(Knife knife) {
        knives.add(knife);
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSort() {
        return isSort;
    }

    public void setSort(boolean sort) {
        isSort = sort;
    }
}
